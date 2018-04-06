(ns clj-blockchain.blockchain
  (require [clojure.string :as str]
           [digest :as digest]
           [clojure.data.json :as json]))


;block = {
;         'index': 1,
;         'timestamp': 1506057125.900785,
;         'transactions': [
;                          {
;                           'sender': "8527147fe1f5426f9dd545de4b27ee00",
;                           'recipient': "a77f5cdfa2934df3954a5c7c7da5df1f",
;                           'amount': 5,
;                           }
;                          ],
;         'proof': 324984774000,
;         'previous_hash': "2cf24dba5fb0a30e26e83b2ac5b9e29e1b161e5c1fa7425e73043362938b9824"
;         }


; (def bc {:chain [] :current-transactions []})

(defn hash-block [block]
  (-> (into (sorted-map) block)
      (json/write-str)
      (digest/sha-256)))

(defn new-block [bc proof & prev-hash]
  (let [index (inc (count (bc :chain)))
        timestamp (System/nanoTime)
        transactions (bc :current-transactions)
        previous-hash (if (nil? prev-hash) (hash-block (last (bc :chain))))
        ]
    (assoc bc :chain
              (conj (bc :chain)
                    {:index index
                     :timestamp timestamp
                     :transactions transactions
                     :proof proof
                     :previous-hash previous-hash}))))

(defn valid-proof? [x y]
  (-> (str x y)
       (digest/sha-256)
       (str/ends-with? "0000")))

(defn proof-of-work [last-proof]
  (first (filter #(valid-proof? last-proof %) (iterate inc 0))))



