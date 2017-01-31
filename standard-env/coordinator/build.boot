(def +project+ 'tmp/coordinator)
(def +version+ "0.2.0-SNAPSHOT")

(set-env!
 :asset-paths #{"resources/public"}
 :resource-paths #{"src/clj" "filters"}
 :source-paths #{"config"}

 ;; :checkouts '[[tmp/coordinator "0.1.0-SNAPSHOT" :module "default" :port 8083]
 ;;              [tmp/greeter "0.1.0-SNAPSHOT" :module "greeter" :port 8088]
 ;;              [tmp/uploader "0.1.0-SNAPSHOT" :module "uploader" :port 8089]]

 :repositories #(conj % ["maven-central" {:url "http://mvnrepository.com"}]
                      ["central" "http://repo1.maven.org/maven2/"])

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
         '[boot.task.built-in :as boot])

(task-options!
 gae/install-service {:project +project+
                      :version +version+}
 pom  {:project     +project+
       :version     +version+
       :description "Example code, boot, miraj, GAE"
       :license     {"EPL" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask build
  "Configure and build servlet or service app"
  [k keep bool "keep intermediate .clj and .edn files"
   p prod bool "production build, without reloader"
   s servlet bool "build a servlet-based app DEPRECATED"
   v verbose bool "verbose"]
  (let [keep (or keep false)
        verbose (or verbose false)]
        ;; mod (str (-> (boot/get-env) :gae :module :name))]
    ;; (println "MODULE: " mod)
    (comp (gae/install-sdk)
          (gae/libs :verbose verbose)
          (gae/appstats :verbose verbose)
          ;; (boot/javac :options ["-source" "1.7", "-target" "1.7"])
          (gae/filters :keep (if prod false keep) :verbose verbose)
          (gae/servlets :keep (if prod false keep) :verbose verbose)
          (gae/logging :log :log4j :verbose verbose)
          (gae/config-service)
          (if prod identity (gae/reloader :keep keep :servlet servlet :verbose verbose))
          (gae/build-sift)
          #_(if servlet
            identity
            (gae/install-service))
          (if prod identity (gae/keep-config))
          (gae/target :servlet servlet :verbose verbose)
          )))

