(ns clj-stackdriver.metrics-test
  (:require [clojure.test :refer :all]
            [clj-stackdriver.metrics :refer :all]))

(def api-key (System/getenv "STACKDRIVER_METRICS_API_KEY"))

(when-not api-key
  (println "export STACKDRIVER_METRICS_API_KEY=\"...\" to run these tests.")
  (System/exit 1))

(defn get-hash-str [data-bytes]
  (apply str 
    (map 
        #(.substring (Integer/toString (+ (bit-and % 0xff) 0x100) 16) 1) 
            data-bytes)))

(let [metric_name 'my_test_metric'
      current-time (-> (System/currentTimeMillis)
                       (quot 1000))
      time-0 current-time
      time-1 (- time-0 60)
      time-2 (- time-1 60)
      data-point-0 {:name metric_name
                   :value (rand-int 1024)
                   :collected_at time-0}
      data-point-1 {:name metric_name
                   :value (rand-int 1024)
                   :collected_at time-1}
      data-point-2 {:name metric_name
                   :value (rand-int 1024)
                   :collected_at time-2}
      data-bytes (.getBytes (str (rand-int 10240)))
      digest-bytes (.digest (java.security.MessageDigest/getInstance "sha1") data-bytes)
      revision (get-hash-str digest-bytes)
      deploy-event-0 {:revision_id revision
                      :deployed_by "unit_test"
                      :deployed_to "test_env"
                      :repository "test_project"}]
                   
  (deftest send-metric-test
    (testing "Send a single custom metric to Stackdriver"
      (is (= 201 (:status (send-metrics api-key [data-point-0])))))
    (testing "Send two custom metrics to Stackdriver"
      (is (= 201 (:status (send-metrics api-key [data-point-1 data-point-2]))))))
  (deftest send-deployevent-test
    (testing "Send a single deploy event to Stackdriver"
      (is (= 201 (:status (send-deployevent api-key deploy-event-0)))))))
