(ns icfp2013.problems
  (:require [icfp2013.client :as c]))

(def problems c/problems)

(defn open? [{:keys [solved timeLeft]
              :or {timeLeft 300}}]
  (and (not solved) (< 0 timeLeft)))

(defn wasted? [{:keys [solved timeLeft]}]
  (and (not solved) (= 0 timeLeft)))

(defn w-size [size]
  (->> (problems)
    (filter open?)
    (filter #(= (:size %1) size))
    (sort-by :id)))

(defn all-wasted []
  (->> (problems)
    (filter wasted?)))
