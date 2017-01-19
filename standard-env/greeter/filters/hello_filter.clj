(ns hello-filter
  (:import (javax.servlet Filter FilterChain FilterConfig
                          ServletRequest ServletResponse))
  (:require [clojure.tools.logging :as log :refer [debug info]] ;; :trace, :warn, :error, :fatal
            [ns-tracker.core :refer :all]))

(defn -init [^Filter this ^FilterConfig cfg]
  (log/info "hello-filter init invoked"))

(defn -destroy [^Filter this]
  (log/info "hello-filter destroy invoked"))

#_(defn make-dofilter-method
  "Turns a handler into a function that takes the same arguments and has the
  same return value as the doFilter method in the servlet.Filter class."
  [handler]
  (fn [^Filter this
       ^HttpServletRequest request
       ^HttpServletResponse response
       ^FilterChain filter-chain]
    (let [request-map (-> request
                          (ring/build-request-map)
                          (ring/merge-servlet-keys servlet request response))]
      (if-let [response-map (handler request-map)]
        (.doFilter
         filter-chain
         request
         (update-servlet-response response response-map))
        (throw (NullPointerException. "Handler returned nil"))))))

(defn -doFilter
  [^Filter this
   ^ServletRequest rqst
   ^ServletResponse resp
   ^FilterChain chain]
  (log/info "INBOUND:  hello-filter on: " (str (.getMethod rqst) " " (.getRequestURL rqst)))
  (.doFilter chain rqst resp)
  (log/info "outbound: hello-filter on: " (str (.getMethod rqst) " " (.getRequestURL rqst))))
