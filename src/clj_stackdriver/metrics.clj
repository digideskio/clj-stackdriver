(ns clj-stackdriver.metrics
  (:require [clj-http.client :as client]
            clj-http.util
            [cheshire.core :refer [generate-string]]))

(def uri-base "https://custom-gateway.stackdriver.com/v1/")

(defn get-uri
  "The full URI of a particular resource, by path fragments."
  [& path-fragments]
  (apply str uri-base 
         (interpose "/" (map (comp clj-http.util/url-encode str) 
                             path-fragments))))

(defn request
  "Post the data"
  [api-key uri data]
  (let [post-options {:body data
                      :headers {"x-stackdriver-apikey" api-key}
                      :content-type :json}]
    (client/post uri post-options)))

(defn send-metrics
  "Send the custom metrics"
  [api-key metrics]
    {:pre [(sequential? metrics)]}
    (let [gateway-msg {:timestamp (-> (System/currentTimeMillis) (quot 1000))
                       :proto_version 1
                       :data metrics}
          json-data (generate-string gateway-msg)]
      (request api-key (get-uri "custom") json-data)))

(defn send-deployevent
  "Send a deploy event"
  [api-key event]
    {:pre [(map? event)]}
    (let [json-data (generate-string event)]
      (request api-key (get-uri "deployevent") json-data)))

