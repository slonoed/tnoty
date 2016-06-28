(defproject tnoty "0.1.0-SNAPSHOT"
  :description "TODO"
  :url "TODO"
  :license {:name "TODO: Choose a license"
            :url "http://choosealicense.com/"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 [cheshire "5.5.0"]
                 [http-kit "2.1.18"]
                 [com.stuartsierra/component "0.3.1"]]
  :profiles {:dev {:main user
                   :dependencies [[org.clojure/tools.namespace "0.2.11"]]
                   :source-paths ["dev"]}
             :production {:main tnoty}
             :uberjar {:main tnoty}})
