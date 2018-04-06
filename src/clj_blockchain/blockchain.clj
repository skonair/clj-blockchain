(ns clj-blockchain.blockchain
  (require [clojure.string :as str]
           [digest :as digest]
           [clojure.data.json :as json]))


(defn hash-block [block]
  (-> (into (sorted-map) block)
      (json/write-str)
      (digest/sha-256)))

(defn new-block [bc proof & prev-hash]
  (let [index (inc (count (bc :chain)))
        timestamp (System/nanoTime)
        transactions (bc :current-transactions)
        previous-hash (if (nil? prev-hash) (hash-block (last (bc :chain))) prev-hash)
        ]
    (assoc bc :chain
              (conj (bc :chain)
                    {:index index
                     :timestamp timestamp
                     :transactions transactions
                     :proof proof
                     :previous-hash previous-hash})
              :current-transactions [])))

(defn new-transaction [bc sender recipient amount]
  (assoc bc :current-transactions
            (conj (bc :current-transactions)
                  {:sender sender
                   :recipient recipient
                   :amount amount})))

(defn valid-proof? [x y]
  (-> (str x y)
       (digest/sha-256)
       (str/ends-with? "0000")))

(defn proof-of-work [last-proof]
  (first (filter #(valid-proof? last-proof %) (iterate inc 0))))



