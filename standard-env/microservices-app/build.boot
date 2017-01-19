(def +project+ 'tmp/services-app)
(def +version+ "0.2.0-SNAPSHOT")

(set-env!
 :gae {:app-id +project+
       :version +version+}

 ;; FIXME: mv to edn file?
 :checkouts '[[tmp.services/main "0.2.0-SNAPSHOT" :module "default" :port 8083]
              [tmp/greeter "0.1.0-SNAPSHOT" :module "greeter" :port 8088]
              [tmp/uploader "0.1.0-SNAPSHOT" :module "uploader" :port 8089]]

 ;; :asset-paths #{"assets"}

 :source-paths #{"config"}

 :repositories {"clojars" "https://clojars.org/repo"
                "central" "http://repo1.maven.org/maven2/"}

 :dependencies '[[migae/boot-gae "0.1.0-SNAPSHOT" :scope "test"]
                 [com.google.appengine/appengine-java-sdk LATEST :scope "test" :extension "zip"]])
                 ;; [tmp.services/main "0.2.0-SNAPSHOT"]
                 ;; [tmp/greeter "0.1.0-SNAPSHOT"]])

(require '[migae.boot-gae :as gae])
