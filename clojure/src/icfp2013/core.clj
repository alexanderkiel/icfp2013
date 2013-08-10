(ns icfp2013.core
  (:require [clojure.tools.namespace.repl :refer (refresh)]
            [clojure.string :as str]
            [clojure.pprint :refer (pprint)]
            [icfp2013.util :refer (to-hex to-bin from-hex gen-inputs bit-count)]
            [icfp2013.client :as c]
            [icfp2013.problems :as problems]
            [icfp2013.program :as p]
            [icfp2013.eval :as eval :refer (not shl1 shr1 shr4 shr16
                                             or and xor plus if0 lambda fold)])
  (:refer-clojure :exclude [not or and]))

(defn fit
  "Calculates the fitness of the program in respect to inputs and outputs.

   The fitness is a value between 0 and 1."
  [program inputs outputs]
  {:pre [(= (count inputs) (count outputs))]}
  (let [p-outputs (map program inputs)
        different-bits (map bit-xor outputs p-outputs)
        different-bit-count (reduce + (map bit-count different-bits))]
    (- 1 (/ different-bit-count (* 64 (count outputs))))))

(def inputs (gen-inputs))

(defn train [size inputs]
  (let [train-result (c/train size)
        id (:id train-result)
        challenge (:challenge train-result)
        operators (->> (:operators train-result) (map (partial symbol "p")) (map eval))
        outputs (c/eval id inputs)]
    (println "challenge:" challenge "id:" id)
    (let [progs (repeatedly #(let [prog (p/gen (vec operators) size)
                                   fit (fit (p/eval prog) inputs outputs)]
                               (println "candiate prog:" (.toString prog) "fit:" fit)
                               {:prog prog
                                :fit fit}))
          win-prog (.toString (:prog (first (drop-while #(> 1 (:fit %1)) progs))))]
      (merge {:id id
              :challenge challenge
              :win-prog win-prog}
        (c/guess id win-prog)))))

