(ns ronniedroid.dayfour
  (:gen-class)
  (:require [clojure.string :as str]
            [clojure.set :as set]))

(def input (slurp "resources/dayfourinput.txt"))

(defn get-pairs
  [coll]
  (->> coll
       (mapv #(read-string %))
       (partition 2)
       (mapv #(range (first %) (+ 1 (last %))))
       (partition 2)))

(defn get-contains-or-overlaps
  [coll c]
  (let [a (sort (first coll))
        b (sort (second coll))
        a-length (count a)
        b-length (count b)
        smaller (if (< a-length b-length) a b)
        results (->> (set/intersection (set a)
                                       (set b)))]
    (if (= c "contains")
      (= smaller (sort results))
      results)))

(defn parse-input
  [s]
  (-> s
      (str/replace #"\n|-|," " ")
      str/trim
      (str/split #" ")
      get-pairs))

(defn part-one
  [coll]
  (->> coll
       parse-input
       (map
        #(get-contains-or-overlaps % "contains"))
       (filter #(true? %))
       count))

(defn part-two
  [coll]
  (->> coll
       parse-input
       (map
        #(get-contains-or-overlaps % "overlaps"))
       (filter #(< 0 (count %)))
       count))
