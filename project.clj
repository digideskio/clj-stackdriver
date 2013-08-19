(defproject clj-stackdriver "0.1.1"
  :description "Clojure interface for the Stackdriver service"
  :url "https://github.com/mentalblock/clj-stackdriver"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [clj-http "0.7.5"]
                 [cheshire "5.2.0"]]
  :main clj-stackdriver.metrics)
