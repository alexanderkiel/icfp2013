(ns icfp2013.problems
  (:require [icfp2013.client :as c]))

(def problems c/problems)

(defn open? [problem]
  (not (:solved problem)))

(defn w-size [size]
  (->> (problems)
    (filter open?)
    (filter #(= (:size %1) size))))
