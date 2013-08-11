(defproject icfp2013 "0.1-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojure/tools.namespace "0.2.4"]
                 [org.clojure/data.json "0.2.2"]
                 [org.clojure/math.combinatorics "0.0.4"]
                 [clj-http "0.7.6"]
                 [slingshot "0.10.3"]
                 [icfp2013/generator "0.1-SNAPSHOT"]]
  :main icfp2013.core
  :jvm-opts ["-Xmx2g" "-server" "-XX:MaxPermSize=300m"])
