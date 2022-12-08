(ns ronniedroid.dayone
  (:gen-class)
  (:require [clojure.string :as str]))

(def input (slurp "resources/dayoneinput.txt"))

(def parsed-input (str/split input #"\n\n"))

(defn innermap
  [coll]
  (->> coll
       (mapv #(read-string %))
       (reduce +)))

(defn part-one
  [coll]
  (->> coll
       (mapv #(str/split % #"\n"))
       (mapv #(innermap %))
       (sort >)
       (apply max)))

(defn part-two
  [coll]
  (->> coll
       (mapv #(str/split % #"\n"))
       (mapv #(innermap %))
       (sort >)
       (take 3)
       (reduce +)))
