(ns clj-blockchain.blockchain-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :as mock]
            [clj-blockchain.blockchain :refer :all]))

(def bc {:chain [] :current-transactions []})

(deftest test-app
  (testing "genesis-block"
      (is (not (nil? (new-block bc 100 1))))))

(deftest test-app
  (testing "valid-chain?"
      (is (valid-chain? {} (new-block bc 100 1)))))

