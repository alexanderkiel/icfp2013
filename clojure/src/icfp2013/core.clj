(ns icfp2013.core
  (:require [clojure.tools.namespace.repl :refer (refresh)]
            [clojure.pprint :refer (pprint)]
            [icfp2013.util :refer (to-hex from-hex)]
            [icfp2013.client :as c]
            [icfp2013.problems :as problems]
            [icfp2013.eval :as eval :refer (not shl1 shr1 shr4 shr16
                                             or and xor plus if0 lambda fold)])
  (:refer-clojure :exclude [not or and]))

(comment
  "Use (refresh) to refresh the project.

   Use (pprint (problems/w-size 3)) to list all problems with size 3.")
