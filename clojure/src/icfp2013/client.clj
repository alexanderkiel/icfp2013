(ns icfp2013.client
  (:require [clj-http.client :as c]
            [clojure.data.json :as json]
            [slingshot.slingshot :refer (try+ throw+)]
            [icfp2013.util :refer (to-hex from-hex)])
  (:refer-clojure :exclude [eval]))

(defn uri [path]
  (str "http://icfpc2013.cloudapp.net/" path "?auth=0346eBiCRDFzfUqcEJSHQrAM2MvLPojl7V373Vs8vpsH1H"))

(defn sleep []
  (println "Got a 429 - sleep for 4s...")
  (Thread/sleep 4000)
  (println "Try again..."))

(defn eval
  "Posts the id and numeric arguments returning a coll of numeric outputs."
  [id args]
  (try+
    (->> (c/post (uri "eval")
           {:body (json/write-str {:id id
                                   :arguments (map to-hex args)})
            :content-type :json
            :as :json})
      :body :outputs (map from-hex))
    (catch Object e
      (if (= 429 (:status e))
        (do
          (sleep)
          (eval id args))
        (throw+)))))

(defn- transform-guess-values [body]
  (if (:values body)
    (update-in body [:values ] (fn [values]
                                 {:input (from-hex (first values))
                                  :output-challenge (from-hex (second values))
                                  :output-guess (from-hex (nth values 2))}))
    body))

(defn guess
  "Posts the id and programm returning the body."
  [id program]
  (try+
    (-> (c/post (uri "guess")
          {:body (json/write-str {:id id
                                  :program program})
           :content-type :json
           :as :json})
      :body transform-guess-values)
    (catch Object e
      (if (= 429 (:status e))
        (do
          (sleep)
          (guess id program))
        (throw+)))))

(defn train
  ([size]
    (train size []))
  ([size operators]
  (try+
    (->> (c/post (uri "train")
           {:body (json/write-str {:size size
                                   :operators operators})
            :content-type :json
            :as :json})
      :body )
    (catch Object e
      (if (= 429 (:status e))
        (do
          (sleep)
          (train size))
        (throw+))))))

(defn problems []
  (try+
    (-> (c/get (uri "myproblems") {:as :json})
      :body )
    (catch Object e
      (if (= 429 (:status e))
        (do
          (sleep)
          (problems))
        (throw+)))))
