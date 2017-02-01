(def +project+ 'tmp.gae/greeter)
(def +version+ "0.1.0-SNAPSHOT")

(set-env!
 :asset-paths #{"resources/public"}
 :resource-paths #{"src/clj" "filters"}
 :source-paths #{"config"}

 :repositories #(conj % ["maven-central" {:url "http://mvnrepository.com"}]
                      ["central" "http://repo1.maven.org/maven2/"])

 :dependencies   '[[org.clojure/clojure "1.8.0" :scope "runtime"]
                   [org.clojure/tools.logging "0.3.1"]

                   [migae/boot-gae "0.2.0-SNAPSHOT" :scope "test"]

                   [javax.servlet/servlet-api "2.5" :scope "provided"]

                   ;; this is for the GAE runtime (NB: scope provided):
                   [com.google.appengine/appengine-java-sdk RELEASE :scope "provided" :extension "zip"]

                   ;; ;; this is required for gae appstats (NB: scope runtime, not provided?):
                   [com.google.appengine/appengine-api-labs RELEASE :scope "runtime"]

                   ;; this is for the GAE services like datastore (NB: scope runtime):
                   ;; (required for appstats, which uses memcache)
                   [com.google.appengine/appengine-api-1.0-sdk RELEASE :scope "runtime"]

                   ;; [org.mobileink/migae.datastore "0.3.3-SNAPSHOT" :scope "runtime"]

                   [hiccup/hiccup "1.0.5"]
                   [cheshire/cheshire "5.7.0"]
                   [compojure/compojure "1.5.2"]
                   [ring/ring-core "1.5.1"]
                   [ring/ring-devel "1.5.1" :scope "test"]
                   [ring/ring-servlet "1.5.1"]
                   [ring/ring-defaults "0.2.1"]
                   [ns-tracker/ns-tracker "0.3.0"]
                   ])

(require '[migae.boot-gae :as gae]
         '[boot.task.built-in :as builtin])

(task-options!
 gae/install-service {:project     +project+
                      :version     +version+}
 pom  {:project     +project+
       :version     +version+
       :description "Example code, boot, GAE"
       :license     {"EPL" "http://www.eclipse.org/legal/epl-v10.html"}})

(deftask build
  "Configure and build servlet or service app"
  [k keep bool "keep intermediate .clj and .edn files"
   p prod bool "production build, without reloader"
   s servlet bool "build a servlet-based app DEPRECATED"
   u unit-test bool "build for unit testing, otherwise for integration testing"
   v verbose bool "verbose"]
  (let [keep (or keep false)
        verbose (or verbose false)]
    (comp (gae/install-sdk)
          (gae/libs :verbose verbose)
          (gae/appstats :verbose verbose)
          (gae/filters :keep keep :verbose verbose)
          (gae/servlets :keep keep :verbose verbose)
          (gae/logging :log :log4j :verbose verbose)
          (if prod identity (gae/reloader :unit-test unit-test
                                          :keep keep :servlet servlet :verbose verbose))
          (gae/config-service :unit-test unit-test)
          (gae/build-sift :unit-test unit-test)
          ;; (if servlet identity (gae/install-service))
          (gae/install-service)
          (if prod identity (gae/keep-config))
          )))

(deftask monitor
  "monitor"
  []
  (gae/monitor :dir "../coordinator"))
