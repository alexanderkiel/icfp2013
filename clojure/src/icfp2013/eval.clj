(ns icfp2013.eval
  (:require [clojure.tools.namespace.repl :refer (refresh)]
            [clojure.pprint :refer (pprint)])
  (:import [icfp2013.util UnsignedMath])
  (:refer-clojure :exclude [not or and]))

;; op1
(def not clojure.core/bit-not)
(def shl1 #(bit-shift-left %1 1))
(def shr1 #(UnsignedMath/bitShiftRight %1 1))
(def shr4 #(UnsignedMath/bitShiftRight %1 4))
(def shr16 #(UnsignedMath/bitShiftRight %1 16))

;; op2
(def or clojure.core/bit-or)
(def and clojure.core/bit-and)
(def xor clojure.core/bit-xor)
(defn plus [x y] (UnsignedMath/plus x y))

(defn if0 [x then else]
  (if (= 0 x) then else))

(defmacro lambda [x e]
  `(fn [~@x] ~e))

(defn- to-byte [x]
  (bit-and 0x00000000000000FF x))

(defn- to-bytes
  "Returns a coll of the bytes of x."
  [x]
  (->> (range 0 8)
    (map #(to-byte (bit-shift-right x (* 8 %1))))))

(defn fold
  "Folds lambda over each byte of x starting with start."
  [x start lambda]
  (reduce (fn [res x] (lambda x res)) start (to-bytes x)))
