(ns org.pilosus.clj-gol
  (:gen-class)
  (:require [clojure.string :as string]))

;; Files

(defn file->board
  "Slurp text file, parse to a 2D vector of integers (board)"
  [path]
  (let [vectors (->> path
                     slurp
                     string/split-lines
                     (map #(string/split % #"\s+")))]
    (loop [vs (seq vectors)
           result []]
      (if vs
        (let [v (first vs)
              ints (mapv #(Integer/parseInt %) v)]
          (recur (next vs) (conj result ints)))
        result))))

;; Game of Life logic

(defn get-cell
  "Return value of the cell given the board and row and column indices"
  [board row col]
  (let [r (nth board row)
        c (nth r col)]
    c))

(defn count-neighbours
  "Return number of live cells that are neighbours for a given cell"
  [board row col]
  (let [rows (count board)
        cols (-> board first count)
        row-above (mod (dec row) rows)
        row-below (mod (inc row) rows)
        col-before (mod (dec col) cols)
        col-after (mod (inc col) cols)]
    (+
     ;; row above
     (get-cell board row-above col-before)
     (get-cell board row-above col)
     (get-cell board row-above col-after)
     ;; same row
     (get-cell board row col-before)
     (get-cell board row col-after)
     ;; row below
     (get-cell board row-below col-before)
     (get-cell board row-below col)
     (get-cell board row-below col-after))))

(defn next-cell
  "Return next generation of the cell"
  [board row col]
  (let [neighbours (count-neighbours board row col)
        current (get-cell board row col)]
    (cond
      ;; reproduction
      (and (= current 0) (= neighbours 3)) 1
      ;; death
      (and (= current 1)
           (or (< neighbours 2)
               (> neighbours 3))) 0
      ;; status quo
      :else current)))

(defn evolve-board
  "Return a new board with the new generation of cells"
  [board]
  (reduce
   ;; process indexed rows first
   (fn [init-val coll-val]
     (let [[row-idx row] coll-val]
       (conj
        init-val
        (reduce
         ;; process pairs [[col-idx element]]
         (fn [init-val-inner coll-val-inner]
           (let [[col-idx _] coll-val-inner
                 next-gen (next-cell board row-idx col-idx)]
             (conj init-val-inner next-gen)))
         [] ;; init-val-inner
         (map-indexed vector row)))))
   [] ;; init-val
   (map-indexed vector board)))

;; Representation

(def ANSI-COLORS
  {:reset "\u001B[0m"
   :black "\u001B[30m"
   :red "\u001B[31m"
   :green "\u001B[32m"
   :yellow "\u001B[33m"
   :blue "\u001B[34m"
   :magenta "\u001B[35m"
   :cyan "\u001B[36m"
   :white "\u001B[37m"})

(def CELL-CHAR {0 "░" 1 "▓"})

(defn- clear-screen
  "Clear terminal screen and move cursor to the top left corner"
  []
  (print (str (char 27) "[2J"))
  (print (str (char 27) "[;H")))

(defn format-cell
  "Substitute integer value of a cell with a character and colorize"
  [cell color-live color-dead]
  (let [char (get CELL-CHAR cell)
        color (if (= cell 1) color-live color-dead)
        ansi-color (get ANSI-COLORS color)]
    (format "%s%s" ansi-color char)))

(defn format-row
  [row color-live color-dead]
  (let [formatted-row (map #(format-cell % color-live color-dead) row)]
    (string/join " " formatted-row)))

(defn print-board
  "Print board"
  [board color-live color-dead]
  (let [rows (map #(format-row % color-live color-dead) board)
        result (string/join "\n" rows)]
    (println result)))

;; Runner

(defn run-game
  [{:keys [seed delay color-live color-dead]
    :or   {seed "resources/seeds/basic.txt"
           delay 333
           color-live :blue
           color-dead :yellow}}]
  (loop [board (file->board seed)]
    (clear-screen)
    (print-board board color-live color-dead)
    (Thread/sleep delay)
    (let [next-generation (evolve-board board)]
      (recur next-generation))))

;; Entrypoint

(defn -main
  "Run the game"
  [& args]
  (prn args)
  (prn (type args))
  (let [seed (or (first args) "resources/seeds/basic.txt")
        delay (try (Integer/parseInt (second args)) (catch Exception _ 333))
        color-live (or (keyword (nth args 2 nil)) :blue)
        color-dead (or (keyword (nth args 3 nil)) :yellow)]
    (run-game {:seed seed
               :delay delay
               :color-live color-live
               :color-dead color-dead})))
