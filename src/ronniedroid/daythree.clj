(ns ronniedroid.daythree
  (:gen-class)
  (:require [clojure.string :as str]))

(def input (slurp "resources/daythreeinput.txt"))

(def abc
  (->
    "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
    (str/split #"")
    (->> (map #(keyword %)))
    (interleave (range 1 53))
    (->> (partition 2)
         (map vec)
         (into {}))))

(def parsed-input
  (-> input
      (str/split #"\n")))

(defn make-keys-get-val
  [coll]
  (->> coll
       (map #(keyword %))
       (map #(get abc %))))

(defn split-and-compair
  [coll]
  (let [col (map #(vec (str/split % #"")) coll)
        a (first col)
        b (second col)
        c (last col)]
    (->> a
         (filter (set b))
         (filter (set c))
         (distinct))))

(defn part-one
  [coll]
  (->> coll
       (map #(str/split % #""))
       (map #(make-keys-get-val %))
       (map #(let [len (/ (count %) 2)]
               (split-at len %)))
       (map #(let [a (first %)
                   b (second %)]
               (filter (set a) b)))
       (map #(distinct %))
       (flatten)
       (apply +)))

(defn part-two
  [coll]
  (->> coll
       (partition 3)
       (map #(split-and-compair %))
       (flatten)
       (make-keys-get-val)
       (apply +)))
