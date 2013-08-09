(ns icfp2013.core
  (:require [clojure.tools.namespace.repl :refer (refresh)]
            [clojure.pprint :refer (pprint)]
            [icfp2013.problems :as problems]))

(println
  "Use (refresh) to refresh the project.

Use (pprint (problems/w-size 3)) to list all problems with size 3.")
