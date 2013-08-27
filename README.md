# clj-stackdriver

A Clojure library for interacting with [Stackdriver](http://www.stackdriver.com/).

## Installation

`clj-stackdriver` is available as a Maven artifact from
[Clojars](http://clojars.org/clj-stackdriver):

```clojure
[clj-stackdriver "0.1.1"]
```

## Usage

````clojure
;; Add the lib to your ns statement
(ns my.ns
  (:require [clj-stackdriver.metrics :as sd]))

;; Some example data points
(def current-time (-> (System/currentTimeMillis) (quot 1000)))
(def data-point-01 {:name 'my_test_metric'
                    :value (rand-int 1024)
                    :collected_at current-time})
(def data-point-02 {:name 'my_test_metric'
                    :value (rand-int 1024)
                    :collected_at (- current-time 60)})
(def deploy-event-01 {:revision_id "mytestrevisionnumber"
                      :deployed_by "unit_test"
                      :deployed_to "test_env"
                      :repository "test_project"})

;; pass it an api key and an event map
(sd/send-deployevent "my-api-key" deploy-event-01)

;; pass it an api key and a vector of metric maps
(sd/send-metrics "my-api-key" [data-point-01 data-point-02])
````

Please see the [custom metrics
](http://feedback.stackdriver.com/knowledgebase/articles/181488-sending-custom-metrics-to-the-stackdriver-system)
and the [deploy
events](http://feedback.stackdriver.com/knowledgebase/articles/212917-sending-code-deploy-events-to-stackdriver)
articles for more information about accepted fields.

Sending multiple metrics in a single call to `send-metrics` is recommended.
Every call to `send-metrics` requires an HTTPS connection and its associated
overhead.

The functions return [clj-http](https://clojars.org/clj-http) responses and
exceptions.

## Development

To run the tests:

    $ export STACKDRIVER_METRICS_API_KEY="your-api-key"
    $ lein deps
    $ lein test

## Credit

This interface was inspired by the [clj-librato](https://clojars.org/clj-librato) library.

## License

Copyright © 2013 Brian Wong

Distributed under the Eclipse Public License, the same as Clojure.
