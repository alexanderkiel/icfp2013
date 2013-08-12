(ns icfp2013.problems
  (:require [clojure.set :refer (intersection)]
            [icfp2013.client :as c]))

(def problems c/problems)

(defn open? [{:keys [solved timeLeft]
              :or {timeLeft 300}}]
  (and (not solved) (< 0 timeLeft)))

(defn wasted? [{:keys [solved timeLeft]}]
  (and (not solved) (= 0 timeLeft)))

(defn w-size
  ([size] (w-size size []))
  ([size skip-ops]
    {:pre [(number? size) (sequential? skip-ops)]}
    (->> (problems)
      (filter open?)
      (filter #(= (:size %) size))
      (filter #(empty? (intersection (set skip-ops) (set (:operators %)))))
      (sort-by :id ))))

(defn w-id [id]
  (->> (problems)
    (filter #(= (:id %) id))
    first))

(defn all-wasted []
  (->> (problems)
    (filter wasted?)))

(defn open-up-to [size skip-ops]
  (->> (problems)
    (filter open?)
    (filter #(<= (:size %) size))
    (filter #(empty? (intersection (set skip-ops) (set (:operators %)))))
    (sort-by :id )))
