(ns icfp2013.problems
  (:require [clj-http.client :as c]
            [clojure.data.json :as json]))

(def uri "http://icfpc2013.cloudapp.net/myproblems?auth=0346eBiCRDFzfUqcEJSHQrAM2MvLPojl7V373Vs8vpsH1H")

(defn fetch-problems []
  (-> uri
    c/get
    :body
    (json/read-str :key-fn keyword)))

(def problems (memoize fetch-problems))

(defn open? [problem]
  (not (:solved problem)))

(defn w-size [size]
  (->> (problems)
    (filter open?)
    (filter #(= (:size %1) size))))
