(ns icfp2013.program
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
