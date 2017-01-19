(ns upload-filter
  (:import (javax.servlet Filter FilterChain FilterConfig
                          ServletRequest ServletResponse))
  (:require [clojure.tools.logging :as log :refer [debug info]] ;; :trace, :warn, :error, :fatal
            [ns-tracker.core :refer :all]))

(defn -init [^Filter this ^FilterConfig cfg]
  (log/info "upload-filter init invoked"))

(defn -destroy [^Filter this]
  (log/info "upload-filter destroy invoked"))

(defn -doFilter
  [^Filter this
   ^ServletRequest rqst
   ^ServletResponse resp
   ^FilterChain chain]
  (log/info "inbound:  upload-filter on: " (str (.getMethod rqst) " " (.getRequestURL rqst)))
  (.doFilter chain rqst resp)
  (log/info "outbound: upload-filter on: " (str (.getMethod rqst) " " (.getRequestURL rqst)))
  )
