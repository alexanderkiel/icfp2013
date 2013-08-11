(ns icfp2013.core
  (:require [clojure.tools.namespace.repl :refer (refresh)]
            [clojure.string :as str]
            [clojure.pprint :refer (pprint)]
            [clojure.repl :refer (pst)]
            [clojure.walk :refer (prewalk postwalk)]
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

(defn solve-rand [id size operators inputs outputs]
  (merge {:id id}
    (loop [inputs inputs
           outputs outputs]
      (let [result (gen-and-guess id operators size inputs outputs)]
        (if-let [values (:values result)]
          (do
            (println values)
            (recur (conj inputs (:input values)) (conj outputs (:output-challenge values))))
          result)))))

(defn energy [s inputs outputs]
  (float (- 1 (p/fit (eval s) inputs outputs))))

(def op1s ['not 'shl1 'shr1 'shr4 'shr16])
(def op1? (apply hash-set op1s))
(def op2s ['and 'or 'xor 'plus])
(def op2? (apply hash-set op2s))

(defn mutate-op1 [op]
  (let [new-op (if (< (rand) 0.5) (rand-nth op1s) op)]
    (if (= op new-op)
      op
      (with-meta new-op {:changed true}))))

(defn mutate-op2 [op]
  (if (< (rand) 0.5)
    (rand-nth op2s)
    op))

(defn mutate-const [c]
  (if (< (rand) 0.2)
    (if (= 0 c) 1 0)
    c))

(defn mutate-list [n]
  (if (= 'lambda (first n))
    n
    (if (clojure.core/and (= 3 (count n)) (< (rand) 0.2))
      (list (first n) (nth n 2) (nth n 1))
      n)))

(defn mutate-node [n]
  (println n)
  (cond
    (list? n) (mutate-list n)
    (op1? n) (mutate-op1 n)
    (op2? n) (mutate-op2 n)
    (#{0 1} n) (mutate-const n)
    :else n))

(defn neighbour [s]
  (postwalk mutate-node s))

(defn p [e e1 t]
  (if (< e1 e)
    1
    (Math/exp (* -1 (/ (- e1 e) t)))))

(defn t [t0 k]
  (* t0 (Math/pow 0.95 k)))

(defn solve-sa
  "Simulated annealing solver."
  [id size operators inputs outputs]
  (loop [s (p/read (p/gen (vec operators) size))
         e (energy s inputs outputs)
         k 1]
    (if (clojure.core/or (= k 30) (= e 0))
      s
      (let [t (t 100 k)]
        (printf "e: %1.4f t: %3.2f s: %s%n" e t s)
        (let [s1 (neighbour s)
              e1 (energy s1 inputs outputs)]
          (if (> (p e e1 t) (Math/random))
            (recur s1 e1 (inc k))
            (recur s e (inc k))))))))

(defn solve [{:keys [id size operators]} solver inputs]
  (let [operators (->> operators (map (partial symbol "p")) (map eval))
        outputs (c/eval id inputs)]
    (solver id size operators inputs outputs)))

(defn train
  ([size inputs]
    (train solve-rand size [] inputs))
  ([solver size operators inputs]
    (let [train-result (c/train size operators)]
      (println "challenge:" (:challenge train-result) "id:" (:id train-result))
      (solve train-result solver inputs))))
