(ns icfp2013.util-test
  (:require [clojure.test :refer :all ]
            [icfp2013.util :refer :all ])
  (:refer-clojure :exclude [not or and]))

(deftest from-hex-test
  (are [result x] (= result (from-hex x))
    1 "0x1"
    2 "0x2"
    0xFFFFFFFFFFFFFFF "0xFFFFFFFFFFFFFFF"
    -1 "0xFFFFFFFFFFFFFFFF"))
