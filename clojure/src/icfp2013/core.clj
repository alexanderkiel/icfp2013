(ns icfp2013.core
  (:require [clojure.tools.namespace.repl :refer (refresh)]
            [clojure.string :as str]
            [clojure.pprint :refer (pprint)]
            [clojure.repl :refer (pst)]
            [icfp2013.util :refer (to-hex to-bin from-hex gen-inputs)]
            [icfp2013.client :as c]
            [icfp2013.problems :as problems]
            [icfp2013.program :as p]
            [icfp2013.eval :as eval :refer (not shl1 shr1 shr4 shr16
                                             or and xor plus if0 lambda fold)])
  (:refer-clojure :exclude [not or and]))

(def inputs (gen-inputs))

(defn gen-and-fit [operators size inputs outputs]
  (let [prog (p/gen (vec operators) size)
        fit (p/fit (p/eval prog) inputs outputs)]
    (println "fit:" (format "%1.4f" (float fit)) "prog:" (.toString prog))
    {:prog prog
     :fit fit}))

(defn gen-and-guess [id operators size inputs outputs]
  (let [progs (repeatedly #(gen-and-fit operators size inputs outputs))
        win-prog (.toString (:prog (first (drop-while #(> 1 (:fit %1)) progs))))]
    (assoc (c/guess id win-prog) :win-prog win-prog)))

(defn solve [{:keys [id size operators]} inputs]
  (let [operators (->> operators (map (partial symbol "p")) (map eval))
        outputs (c/eval id inputs)]
    (merge {:id id}
      (loop [inputs inputs
             outputs outputs]
        (let [result (gen-and-guess id operators size inputs outputs)]
          (if-let [values (:values result)]
            (do
              (println values)
              (recur (conj inputs (:input values)) (conj outputs (:output-challenge values))))
            result))))))

(defn train
  ([size inputs]
    (train size [] inputs))
  ([size operators inputs]
  (let [train-result (c/train size operators)]
    (println "challenge:" (:challenge train-result) "id:" (:id train-result))
    (solve train-result inputs))))
