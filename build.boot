(set-env!
 :resource-paths #{"src" "resources"}
 :checkouts '[[snow "0.1.0-SNAPSHOT"]]
 :dependencies '[[tolitius/boot-check "0.1.3" :scope "test"]
                 [org.danielsz/system "0.4.2-SNAPSHOT"]
                 [environ "1.1.0"]
                 [boot-environ "1.1.0"]
                 [org.clojure/core.async "0.4.474"]
                 [ring "1.6.2"]
                 [ring/ring-defaults "0.3.1"]
                 [compojure "1.6.0"]
                 [proto-repl "0.3.1"]
                 ;; [org.immutant/immutant-web "1.1.4"]
                 [luminus-immutant "0.2.3"]
                 [org.clojure/tools.nrepl "0.2.13"]
                 [ring-middleware-format "0.7.0"]
                 [lambdacd "0.13.5"]
                 [venantius/pyro "0.1.2"]
                 [lambdaui "1.0.1-SNAPSHOT"]
                 [http-kit "2.2.0"]
                 [clj-http "3.7.0"]
                 [clj-ssh "0.5.14"]
                 [selmer "1.11.1"]
                 [com.jcraft/jsch "0.1.54"]
                 [datawalk "0.1.12"]
                 [cheshire "5.8.0"]
                 [metosin/muuntaja "0.5.0"]
                 [snow "0.1.0-SNAPSHOT"]
                 [lambdacd-git "0.4.1"]
                 [lambdacd-cron "0.1.1"]])

(require
 '[tolitius.boot-check :as check]
 '[environ.boot :refer [environ]]
 '[airplanes.systems :refer [dev-system base-system]]
 '[system.boot :refer [system run]]
 '[system.repl :refer [go reset]])

(deftask dev
  "Run a restartable system in the Repl"
  []
  (comp
   (environ :env {:http-port "9000"})
   (watch :verbose true)
   (system :sys #'base-system :auto true :files ["handler.clj" "html.clj"
                                                 "pipeline.clj" "steps.clj"])
   (repl :server true)))

(deftask dev-run
  "Run a dev system from the command line"
  []
  (comp
   (environ :env {:http-port "9000"})
   (run :main-namespace "airplanes.core" :arguments [#'dev-system])
   (wait)))

(deftask build
  "Builds an uberjar of this project that can be run with java -jar"
  []
  (comp
   (aot :namespace '#{airplanes.core})
   (pom :project 'myproject
        :version "1.0.0")
   (uber)
   (jar :main 'airplanes.core)))
