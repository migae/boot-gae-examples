(ns uploader.core
  (:import [java.io  ByteArrayInputStream InputStreamReader PushbackReader])
  (:refer-clojure :exclude [read])
  (:require [clojure.tools.logging :as log :refer [debug info]] ;; :trace, :warn, :error, :fatal
            [clojure.tools.reader.edn :as edn :refer [read]]
            [compojure.core :refer :all]
            [compojure.route :as route]
            [cheshire.core :as json]
            ;; [ring.util.response :as rsp]
            [ring.util.servlet :as ring]
            [ring.middleware.edn :as redn]
            [ring.middleware.multipart-params :refer :all]
            [ring.middleware.multipart-params.byte-array :refer [byte-array-store]]
            [ring.middleware.params :refer [wrap-params]]
            ;; [ring.middleware.defaults :refer :all]
            ))

(defn stream [s]
   (java.io.PushbackReader.
    (java.io.InputStreamReader.
     (ByteArrayInputStream. s)
     "UTF-8")))

(defn read-stream [s]
  (edn/read
   {:eof :theend}
   s))

(defroutes upload-routes
  (POST "/upload" {mp :multipart-params :as req}
;;        (log/info "POST " (:uri req))
        ;; (log/debug "schemata: " (em/dump-schemata))
        (let [{:keys [filename content-type bytes]} (get mp "file")
              s (stream bytes)]
          (let [edn-seq (repeatedly (partial read-stream s))]
            (dorun (map
                    #(do
                       (log/info "UPLOADED PAYLOAD: ")
                       (if (meta %)
                         (print (str "^" (meta %))))
                       (let [kind (:kind %)
                             type (:type %)
                             ;; schema-kw (:schema %)
                             ;; schema (em/schema type) ;
                             ;; kws (into [] (for [fld schema] (keyword (:name fld))))
                             data (:data %)]
                         (log/info (str "kind: " kind))
                         (log/info (str "type: " type))
                         ;; (log/info (str "schema kw: " schema-kw))
                         ;; (log/info (str "schema: " schema))
                         ;; (log/info (str "kws: " kws))
                         (log/info (str "data: " data))
                         (doseq [datum data]
                           (log/debug "datum" datum))))
                    (take-while (partial not= :theend) edn-seq))))
          (str "uploaded file " filename \newline)
          ))

  (route/not-found "<h1>upload route not found</h1>"))

(ring/defservice
   (-> (routes
        upload-routes)
      wrap-params
      redn/wrap-edn-params
      ;; (wrap-defaults api-defaults)
      (wrap-multipart-params {:store (byte-array-store)})
       ))
