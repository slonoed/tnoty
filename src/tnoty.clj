(ns tnoty
  (:use org.httpkit.server)
  (:require [clojure.string :as str]
            [tnoty.server :as server]
            [com.stuartsierra.component :as component]
            [org.httpkit.client :as http])
  (:gen-class))


(defn create-system [{p :port s :secret t :token i :chat-id}]
  (component/system-map
    :server (server/new-server p s t i)))

(defn -main []
  (let [
        s (or (System/getenv "SECRET_URL") "secret_url")
        t (System/getenv "BOT_TOKEN")
        p (or (System/getenv "PORT") 8080)
        i (System/getenv "CHAT_ID")]
    (cond
      (nil? t) (println "Put BOT_TOKEN to env")
      (nil? i) (println "Put CHAT_ID to env")
      :default (do
                 (component/start (create-system {:port p :secret s :token t :chat-id i}))
                 (printf "System started with port %s, secret path %s, chat id %s, token %s\n" p s i t)))))
