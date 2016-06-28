(ns tnoty.server
  (:use org.httpkit.server)
  (:require [com.stuartsierra.component :as component]))

(defn handler [{secret :secret} req]
  (let [{u :uri q :query-params b :body} req
        ok (= (subs (:uri req) 1) secret)
        qtext (get q "text" "empty-text")]
    (println qtext b)
    {:status (if ok 200 500)
     :headers {"Content-Type" "text/html"}
     :body    (if ok "Ok" "Wrong secret")})
  )

(defrecord Server [port secret token]
  component/Lifecycle
  (start [this]
    (let [server (run-server #(handler this req) {:port port})]
      (assoc this
             :close server)))

  (stop [this]
    (when-let [s (:close this)]
      (s :timeout 100))
    (assoc this
           :close nil)))

(defn new-server [port secret token]
  (map->Server {:port port :secret secret :token token}))
