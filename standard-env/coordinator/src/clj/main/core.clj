(ns main.core
  (:require [clojure.tools.logging :as log :refer [debug info]] ;; :trace, :warn, :error, :fatal
            [compojure.core :refer :all]
            [compojure.route :as route]
            [ring.util.response :as rsp]
            [ring.util.servlet :as ring]
            [ring.middleware.params :refer [wrap-params]]
            [ring.middleware.defaults :refer :all]))

(defroutes main-routes
    (GET "/main" [name :as rqst]
         (do (log/info "main servlet handler:  main.core on " (:request-method rqst)
                      (str (.getRequestURL (:servlet-request rqst))))
             (-> (rsp/response (str "Hello there, from main.core servlet"))
                 (rsp/content-type "text/html"))))
    (route/not-found "<h1>main route not found</h1>"))

(ring/defservice
   (-> (routes
        main-routes)
       (wrap-defaults api-defaults)
       ))
