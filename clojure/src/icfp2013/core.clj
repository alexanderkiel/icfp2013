(ns icfp2013.core
  (:require [clojure.tools.namespace.repl :refer (refresh)]
            [clojure.string :as str]
            [clojure.pprint :refer (pprint)]
            [clojure.repl :refer (pst)]
            [clojure.math.combinatorics :refer (permutations)]
            [clojure.set :refer (union)]
            [clojure.walk :refer (walk prewalk postwalk)]
            [icfp2013.util :refer (to-hex to-bin from-hex gen-inputs)]
            [icfp2013.client :as c]
            [icfp2013.problems :as problems]
            [icfp2013.program :as p]
            [icfp2013.eval :as eval :refer (not shl1 shr1 shr4 shr16
                                             or and xor plus if0 lambda fold)])
  (:refer-clojure :exclude [not or and var?]))

(def inputs (gen-inputs))

(defn gen-and-fit [operators size inputs outputs]
  (let [prog (p/gen (vec operators) (+ size (rand 4)))
        fit (p/fit (p/eval prog) inputs outputs)]
    (println "fit:" (format "%1.4f" (float fit)) "prog:" (.toString prog))
    {:prog prog
     :fit fit}))

(defn solve-rand [id size operators inputs outputs]
  (let [progs (repeatedly #(gen-and-fit operators size inputs outputs))
        win-prog (.toString (:prog (first (drop-while #(> 1 (:fit %1)) progs))))]
    (assoc (c/guess id win-prog) :win-prog win-prog)))

(defn energy [s inputs outputs]
  (float (- 1 (p/fit (eval s) inputs outputs))))

(def op1s ['not 'shl1 'shr1 'shr4 'shr16])
(def op1? (apply hash-set op1s))
(def op2s ['and 'or 'xor 'plus])
(def op2? (apply hash-set op2s))
(def special ['lambda 'if0 'fold])
(def all-symbols (union (set op1s) (set op2s) (set special)))

(defn lambda? [node]
  (clojure.core/and (seq? node) (= 'lambda (first node))))

(defn var? [n]
  (clojure.core/not (all-symbols n)))

(defn arg-list?
  "Returns true iff the node is a lambda arg list."
  [node]
  (clojure.core/and (seq? node) (var? (first node))))

(defn op2-node? [node]
  (clojure.core/and
    (seq? node)
    (= 3 (count node))
    (clojure.core/not (lambda? node))))

(defn if0-node? [node]
  (clojure.core/and
    (seq? node)
    (= 4 (count node))
    (= 'if0 (first node))))

(defn fold-node? [node]
  (clojure.core/and
    (seq? node)
    (= 4 (count node))
    (= 'fold (first node))))

(defn rand?
  "Returns true with probability p."
  [p]
  (> p (rand)))

(defn mutate-op1 [op]
  (if (rand? 0.3)
    (rand-nth op1s)
    op))

(defn mutate-op2 [op]
  (if (rand? 0.3)
    (rand-nth op2s)
    op))

(defn mutate-const [vars c]
  (if (rand? 0.4)
    (rand-nth (conj (vec vars) 0 1))
    c))

(defn mutate-var [vars v]
  (if (rand? 0.2)
    (rand-nth (conj (vec vars) 0 1))
    v))

(def perm-3 (vec (permutations [1 2 3])))

(defn mutate-if0 [n]
  (conj (map (partial nth n) (rand-nth perm-3)) 'if0))

(defn mutate-fold [n]
  (list 'fold (nth n 2) (nth n 1) (nth n 3)))

(defn mutate-list [n]
  (if (rand? 0.2)
    (cond
      (op2-node? n) (list (first n) (nth n 2) (nth n 1))
      (if0-node? n) (mutate-if0 n)
      (fold-node? n) (mutate-fold n)
      :else n)
    n))

(defn mutate-node [vars n]
  (cond
    (seq? n) (mutate-list n)
    (op1? n) (mutate-op1 n)
    (op2? n) (mutate-op2 n)
    (#{0 1} n) (mutate-const vars n)
    (var? n) (mutate-var vars n)
    :else n))

(defn find-vars
  "Returns a set of all vars defined in node. Only lambda nodes define vars."
  [node]
  (if (lambda? node)
    (set (second node))
    (hash-set)))

(defn neighbour
  "Returns a neighbour of prog possibly using vars."
  ([prog] (neighbour (hash-set) prog))
  ([vars prog]
;    (println {:vars vars :prog prog})
    (let [descend (partial neighbour (union vars (find-vars prog)))
          inner (if (arg-list? prog) identity descend)]
      (walk inner identity (mutate-node vars prog)))))

(defn p [e e1 t]
  (if (< e1 e)
    1.0
    (Math/exp (* -1 (/ (- e1 e) t)))))

(defn sa [operators size inputs outputs]
  (loop [s (p/read (p/gen (vec operators) (+ size (rand 4))))
         e (energy s inputs outputs)
         t 0.2
         k 0]
    (if (clojure.core/or (= e 0.0) (< t 0.001))
      [s e k]
      (let [s1 (neighbour s)
            e1 (energy s1 inputs outputs)
            p (p e e1 t)]
;        (printf "e: %1.4f e1: %1.4f t: %3.2f p: %1.2f s: %s s1: %s%n" e e1 t p s s1)
        (if (rand? p)
          (recur s1 e1 (* t 0.99) (inc k))
          (recur s e (* t 0.99) (inc k)))))))

(defn psa [operators size inputs outputs]
  (let [sas (pmap (fn [_] (sa operators size inputs outputs)) (range 0 4))]
    (first (sort-by second sas))))

(defn solve-sa
  "Simulated annealing solver."
  [id size operators inputs outputs]
  (loop [[prog e k] (psa operators size inputs outputs)]
    (println (format "e: %1.4f k: %d prog: %s" e k prog))
    (if (= 0.0 e)
      (assoc (c/guess id (pr-str prog)) :prog (pr-str prog))
      (recur (psa operators size inputs outputs)))))

(defn solve [{:keys [id size operators]} solver inputs]
  (let [operators (->> operators (map (partial symbol "p")) (map eval))]
    (loop [inputs inputs
           outputs (c/eval id inputs)]
      (let [result (solver id size operators inputs outputs)]
        (if-let [values (:values result)]
          (do
            (println values)
            (recur (conj inputs (:input values)) (conj outputs (:output-challenge values))))
          result)))))

(defn train
  ([size inputs]
    (train solve-rand size [] inputs))
  ([solver size operators inputs]
    (let [train-result (c/train size operators)]
      (println "challenge:" (:challenge train-result) "id:" (:id train-result))
      (solve train-result solver inputs))))
