(ns upload-filter
  (:import (javax.servlet Filter FilterChain FilterConfig
                          ServletRequest ServletResponse))
  (:require [ns-tracker.core :refer :all]))

(defn -init [^Filter this ^FilterConfig cfg]
  (println "upload-filter init invoked"))

(defn -destroy [^Filter this]
  (println "upload-filter destroy invoked"))

(defn -doFilter
  [^Filter this
   ^ServletRequest rqst
   ^ServletResponse resp
   ^FilterChain chain]
  (println "inbound:  upload-filter on: " (str (.getMethod rqst) " " (.getRequestURL rqst)))
  (.doFilter chain rqst resp)
  (println "outbound: upload-filter on: " (str (.getMethod rqst) " " (.getRequestURL rqst)))
  )
