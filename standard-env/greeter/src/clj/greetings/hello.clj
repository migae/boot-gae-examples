(ns greetings.hello
  (:refer-clojure :exclude [read read-string])
  (:import #_[com.google.appengine.api.datastore EntityNotFoundException]
           [java.io InputStream ByteArrayInputStream]
           [java.util Collections]
           [java.lang IllegalArgumentException RuntimeException])
  (:require [clojure.tools.logging :as log :refer [debug info]] ;; :trace, :warn, :error, :fatal
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as rsp]
            [ring.util.servlet :as ring]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.defaults :refer :all]))

(defroutes hello-routes
    (GET "/hello/:name" [name :as rqst]
         (do (log/info "hello servlet handler:  greetings.hello on " (:request-method rqst)
                      (str (.getRequestURL (:servlet-request rqst))))
             (-> (rsp/response (str "Hi there from the hello servlet (of the greeter service), " name))
                 (rsp/content-type "text/html"))))
    (GET "/foo/:name" [name :as rqst]
         (do (log/info "hello servlet handler:  greetings.hello on " (:request-method rqst)
                      (str (.getRequestURL (:servlet-request rqst))))
             (-> (rsp/response (str name "?  I pity the foo!"))
                 (rsp/content-type "text/html"))))
    (route/not-found "<h1>Hello route not found</h1>"))

(ring/defservice
   (-> (routes
        hello-routes)
       (wrap-defaults api-defaults)
       ))
