(ns clj-blockchain.handler
  (:require [compojure.core :refer [defroutes POST GET]]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.middleware.defaults :refer [wrap-defaults site-defaults]]
            [ring.middleware.json :as ring-json]
            [clojure.data.json :as json]
            [clj-blockchain.blockchain :as blockchain]))

; the genesis-block
(def bc (atom (blockchain/new-block {:chain [] :current-transactions []} 100 1)))

(defn mine [bc]
  (let [last-block (last (@bc :chain))
        last-proof (last-block :proof)
        proof (blockchain/proof-of-work last-proof)
        new-bc (blockchain/new-transaction @bc "0" "my-node" 1)
        prev-hash (blockchain/hash-block last-block)
        new-block (blockchain/new-block new-bc proof prev-hash)
        last-entry (last (new-block :chain))
        ]

    (reset! bc new-block)
    (json/write-str {
                     :message "New Block Forged"
                     :index (last-entry :index)
                     :transactions (last-entry :transactions)
                     :proof proof
                     :previous-hash prev-hash
                     })))

(defn full-chain [bc]
  (json/write-str {:chain (@bc :chain) :length (count (@bc :chain))}))

(defn open-transaction [bc]
  (json/write-str {:current-transactions (@bc :current-transactions) :length (count (@bc :current-transactions))}))

(defn new-transaction [bc params]
  (let [new-bc (blockchain/new-transaction @bc (params :sender) (params :recipient) (params :amount))]
    (reset! bc new-bc)
    (json/write-str new-bc)))

(defroutes app-routes
  (GET "/" [] "Hello World")
  (GET "/mine" [] (mine bc))
  (GET "/chain" [] (full-chain bc))
  (GET "/transactions/open" [] (open-transaction bc))
  (POST "/transactions/new" {params :params} (new-transaction bc params))
  (route/not-found "Not Found"))

(def app
  (-> (handler/api app-routes)
      ring-json/wrap-json-response
      ring-json/wrap-json-params))
;  (ring-json/wrap-json-body app-routes (assoc-in site-defaults [:security :anti-forgery] false)))
