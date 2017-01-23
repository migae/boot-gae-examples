(def +project+ 'tmp/coordinator)
(def +version+ "0.2.0-SNAPSHOT")

(set-env!
 :gae {:app {:id "microservices-app"
             :dir "../microservices-app"}
       :module {:name "default"
                :version "v1"}}

 :asset-paths #{"resources/public"}
 :resource-paths #{"src/clj" "filters"}
 :source-paths #{"config"}

 :repositories #(conj % ["maven-central" {:url "http://mvnrepository.com"}]
                      ["central" "http://repo1.maven.org/maven2/"])

;;                ["clojars" "https://clojars.org/repo"]

 :dependencies   '[[org.clojure/clojure "1.8.0" :scope "runtime"]
                   [org.clojure/tools.logging "0.3.1"]

                   [migae/boot-gae "0.2.0-SNAPSHOT" :scope "test"]

                   [javax.servlet/servlet-api "2.5" :scope "provided"]

                   [com.google.appengine/appengine-java-sdk RELEASE :scope "provided" :extension "zip"]

                   ;; ;; this is required for gae appstats:
                   ;; [com.google.appengine/appengine-api-labs LATEST :scope "test"]

                   ;; this is for the GAE services like datastore (NB: scope runtime):
                   ;; (required for appstats, which uses memcache)
                   ;; [com.google.appengine/appengine-api-1.0-sdk RELEASE :scope "runtime"]

                   [compojure/compojure "1.4.0"]
                   [ring/ring-core "1.4.0"]
                   [ring/ring-servlet "1.4.0"]
                   [ring/ring-defaults "0.1.5"]
                   [ring/ring-devel "1.4.0" :scope "test"]
                   [ns-tracker/ns-tracker "0.3.0"]
                   ])

(require '[migae.boot-gae :as gae]
         '[boot.task.built-in :as builtin])

(task-options!
 pom  {:project     +project+
       :version     +version+
       :description "Example code, boot, miraj, GAE"
       :license     {"EPL" "http://www.eclipse.org/legal/epl-v10.html"}})

#_(deftask bldtest
  "make a dev build - including reloader"
  [k keep bool "keep intermediate .clj and .edn files"
   v verbose bool "verbose"]
  (comp (gae/install-sdk :verbose verbose)
        (gae/libs :verbose verbose)
        (gae/logging :verbose verbose)
        (builtin/show :fileset true)
        (builtin/sift :to-asset #{#"(.*\.clj$)"}
                      :move {#"(.*\.clj$)" "WEB-INF/classes/$1"})
        ;; (clj)
        ;; (appstats)
        ;; (filters :keep keep)
        ;; (servlets :keep keep)
        ;; (reloader :keep keep)
        ;; (webxml)
        ;;(appengine)
        ))
