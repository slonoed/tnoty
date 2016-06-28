(ns tnoty.server
  (:use org.httpkit.server)
  (:require [com.stuartsierra.component :as component]
            [org.httpkit.client :as http]
            [cheshire.core :as json]))

(defn text-param [a]
  (second (first (filter #(= "text" (first %)) (map #(clojure.string/split % #"=")
                                                    (clojure.string/split a  #"\&"))))))

(defn ->bot [token chat-id text]
  (http/request {:url (str "https://api.telegram.org/bot" token "/sendMessage")
                 :method :post
                 :headers {"Content-Type" "application/json"}
                 :body (json/generate-string
                         {:chat_id chat-id
                          :text text
                          :parse_mode "Markdown"})}
                (fn [{:keys [status body]}]
                  (when-not (= status 200) (println status body)))))

(defn handler [{s :secret t :token i :chat-id} req]
  (let [{u :uri q :query-string b :body} req
        ok (= (subs (:uri req) 1) s)
        qtext (java.net.URLDecoder/decode (text-param q))]
    (println q)
    (->bot t i (str
                 (if qtext (str qtext \newline) "")
                 (if b (slurp b) "")))
    {:status (if ok 200 500)
     :headers {"Content-Type" "text/html"}
     :body    (if ok "Ok" "Wrong secret")}))

(defrecord Server [port secret token chat-id]
  component/Lifecycle
  (start [this]
    (let [server (run-server #(handler this %) {:port port})]
      (assoc this
             :close server)))

  (stop [this]
    (when-let [s (:close this)]
      (s :timeout 100))
    (assoc this
           :close nil)))

(defn new-server [port secret token chat-id]
  (map->Server {:port port :secret secret :token token :chat-id chat-id}))

