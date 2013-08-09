(ns icfp2013.client
  (:require [clj-http.client :as c]
            [clojure.data.json :as json]
            [icfp2013.util :refer (to-hex from-hex)])
  (:refer-clojure :exclude [eval]))

(def uri "http://icfpc2013.cloudapp.net/eval?auth=0346eBiCRDFzfUqcEJSHQrAM2MvLPojl7V373Vs8vpsH1H")

(defn eval
  "Posts the given numeric arguments to the server returning a coll of numeric outputs."
  [id args]
  (->> (c/post uri
        {:body (json/write-str {:id id
                                :arguments (map to-hex args)})
         :content-type :json
         :as :json})
    :body :outputs
    (map from-hex)))

