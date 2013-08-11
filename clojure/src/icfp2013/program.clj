(ns icfp2013.program
  (:require [icfp2013.util :refer (bit-count)])
  (:import [icfp2013.generator ProgramGenerator]
           [icfp2013.generator.syntax Operators]
           [java.util Random])
  (:refer-clojure :exclude [eval read not or and]))

(def not (Operators/NOT))
(def shl1 (Operators/SHL1))
(def shr1 (Operators/SHR1))
(def shr4 (Operators/SHR4))
(def shr16 (Operators/SHR16))
(def and (Operators/AND))
(def or (Operators/OR))
(def xor (Operators/XOR))
(def plus (Operators/PLUS))
(def if0 (Operators/IF0))
(def fold (Operators/FOLD))
(def tfold (Operators/TFOLD))

(defn gen [ops size]
  (-> (ProgramGenerator. ops size)
    (.apply (Random.))))

(defn size [program]
  (.size program))

(defn op [program]
  (.op program))

(defn read [program]
  (-> program
    (.toString)
    (read-string)))

(defn eval [program]
  (-> program
    (.toString)
    (read-string)
    (clojure.core/eval)))

(defn size [prog]
  (cond
    (= 0 prog) 1
    (= 1 prog) 1
    (symbol? prog) 1
    (list? prog) (condp = (first prog)
                   'lambda (inc (size (nth prog 2)))
                   (apply + 1 (map size (rest prog))))))

(defn fit
  "Calculates the fitness of the program in respect to inputs and outputs.

   The fitness is a value between 0 and 1."
  [program inputs outputs]
  {:pre [(= (count inputs) (count outputs))]}
  (let [p-outputs (map program inputs)
        different-bits (map bit-xor outputs p-outputs)
        different-bit-count (reduce + (map bit-count different-bits))]
    (- 1 (/ different-bit-count (* 64 (count outputs))))))
