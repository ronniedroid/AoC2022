(ns ronniedroid.dayseven
  (:gen-class)
  (:require [clojure.string :as str]))

(def input (slurp "resources/dayseveninput.txt"))

(def parsed-input
  (-> input
      (str/replace #"\$ |ls|dir\ [a-zA-Z]" "")
      (str/split #"\n")
      (->> (map #(str/split % #" "))
           (filter #(not (str/blank? (first
                                      %)))))))

(def pwd (atom []))
(def fs (atom {}))

(defn proc
  [coll]
  (doseq [c coll]
    (if (= (first c) "cd")
      (if (= (second c) "..")
        (swap! pwd pop)
        (swap! pwd conj (second c)))
      (if (= (get-in @fs @pwd) nil)
        (swap! fs assoc-in
               (concat @pwd [:files])
               (+ (read-string (first c))))
        (swap! fs update-in
               (concat @pwd [:files])
               +
               (read-string (first c)))))))

(def fs2 (atom {"/" {}}))

(def pwd2 (atom ["/"]))

(swap! pwd2 conj "a")
(swap! pwd2 pop)

(swap! fs2 assoc-in (concat @pwd2 [:files]) 1234)
(swap! fs2 update-in
       (concat @pwd2 [:files])
       +
       1234)

(get-in @fs ["/" "a" "c" :files])
