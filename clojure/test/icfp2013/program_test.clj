(ns icfp2013.program-test
  (:require [clojure.test :refer :all ]
            [icfp2013.program :refer :all ])
  (:refer-clojure :exclude [eval read not or and]))

(deftest size-test
  (are [n prog] (= n (size prog))
    1 0
    1 1
    1 'x
    4 '(if0 0 0 0)
    5 '(fold 0 0 (lambda (x y) 0))
    2 '(not 0)
    2 '(shl1 0)
    2 '(shr1 0)
    2 '(shr4 0)
    2 '(shr16 0)
    3 '(and 0 0)
    3 '(or 0 0)
    3 '(xor 0 0)
    3 '(plus 0 0)))
