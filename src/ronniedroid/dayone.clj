(ns ronniedroid.dayone
  (:gen-class)
  (:require [clojure.java.io :as io]
            [clojure.string :as str]))

(def input
  (slurp (io/resource "dayoneinput.txt")))

(def paresed-input (str/split input #"\n\n"))

(defn innermap
  [coll]
  (->> coll
       (mapv #(read-string %))
       (reduce +)))

(->> paresed-input
     (mapv #(str/split % #"\n"))
     (mapv #(innermap %))
     (sort >)
     (take 3)
     (reduce +))
