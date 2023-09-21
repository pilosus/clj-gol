# org.pilosus/clj-gol

Conway's Game of Life: simple, terminal based implementation in Clojure.

## Installation

Download from https://github.com/pilosus/clj-gol

## Usage

Run the project directly, via `:exec-fn` with sensible defaults:

    $ clojure -X:run-x


Run the project, overriding gameplay options (mind the quotation marks
when passing in a path to a seed file!):

    $ clojure -X:run-x :seed '"resources/seeds/infinite.txt"' \
        :delay 100 \
        :color-live :green \
        :color-dead :white

Run the project directly, via `:main-opts` (`-m org.pilosus.clj-gol`):

    $ clojure -M:run-m

Run the project, overriding the name to be greeted:

    $ clojure -M:run-m resources/seeds/infinite.txt 100 green white


## Options

`:seed` is a path to a text file with the seed. Seed must be
represented in a matrix form. Rows are separated by a new line
character. Elements are separated by a single whitespace. Live cells
represented by `1`, dead cells by `0`.

`:delay` is a number of milliseconds before iterations.

`:color-live` and `:color-dead` is a color name for live cells and
dead cells respectively. Can be on of the following values: `:black`,
`:blue`, `:cyan`, `:green`, `:magenta`, `:red`, `:reset`, `:white`,
`:yellow`.

## Development

Run the project's tests:

    $ clojure -T:build test

Run the project's CI pipeline and build an uberjar:

    $ clojure -T:build ci

Run that uberjar:

    $ java -jar target/org.pilosus/clj-gol-0.1.0-SNAPSHOT.jar

## License

Copyright © 2023 Vitaly Samigullin

See [LICENSE](https://github.com/pilosus/clj-gol/tree/main/LICENSE)

