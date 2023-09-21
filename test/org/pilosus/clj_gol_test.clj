(ns org.pilosus.clj-gol-test
  (:require [clojure.test :refer [deftest is testing]]
            [org.pilosus.clj-gol :as gol]))

(def params-count-neighbours
  [[[[0 0 0]
     [0 1 0]
     [0 0 0]]
    [1 1]
    0
    "No neighbours"]
   [[[1 0 0]
     [0 1 1]
     [0 1 0]]
    [1 1]
    3
    "Row above col before; same row col after; row below col same"]
   [[[0 0 0 1 1]
     [0 0 0 0 0]
     [0 0 0 0 0]
     [0 0 0 0 0]
     [0 0 0 1 0]]
    [0 3]
    2
    "Modulo division check"]])

(deftest count-neighbours-test
  (testing "Count neighbours according to the Game of Life rules\n"
    (doseq [[board [row col] expected description] params-count-neighbours]
      (testing description
        (is (= expected (gol/count-neighbours board row col)))))))
