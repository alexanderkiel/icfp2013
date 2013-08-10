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

(deftest bit-count-test
  (are [result x] (= result (bit-count x))
    1 2r1
    2 2r11
    3 2r111
    1 2r10
    1 2r100
    2 2r1010
    63 -2
    64 -1))
