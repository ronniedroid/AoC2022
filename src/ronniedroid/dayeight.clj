(ns ronniedroid.dayeight
  (:gen-class)
  (:require [clojure.string :as str]))

(def input (slurp "resources/dayeightinput.txt"))

(defn has?
  [coll pred]
  (not= (some #{pred} coll) nil))

(defn find-first [coll f] (first (filter f coll)))

(def rows
  (->> (str/split input #"\n")
       (map #(str/split % #""))
       (map #(map (fn [x] (parse-long x)) %))))

(def columns (apply map list rows))

(defn invisible?
  [coll n]
  (let [current (nth coll n)
        before (take n coll)
        after (nthrest coll (+ n 1))]
    (and
     (if (< current (apply max after))
       true
       (if (and (= current (apply max after))
                (has? (distinct after) current))
         true
         false))
     (if (< current (apply max before))
       true
       (if (and (= current (apply max before))
                (has? (distinct before) current))
         true
         false)))))

(defn apply-invisible
  [coll]
  (reduce
   (fn [[acc n] item]
     [(if (or (= n 0) (>= n (- (count coll) 1)))
        (conj acc 0)
        (if (invisible? coll n)
          (conj acc 1)
          (conj acc 0))) (+ 1 n)])
   [[] 0]
   coll))

(defn invisible-matrix
  [coll]
  (reduce
   (fn [acc list]
     (if (or (= list (first coll))
             (= list (last coll)))
       (conj acc (mapv #(- % %) list))
       (conj acc (first (apply-invisible list)))))
   []
   coll))

(def invisible-rows (invisible-matrix rows))
(def invisible-columns (invisible-matrix columns))

(def invisible-columns-transposed
  (apply map vector invisible-columns))

(def number-of-visible-trees
  (->> (flatten invisible-columns-transposed)
       (interleave (flatten invisible-rows))
       (partition 2)
       (map #(reduce + %))
       (filter #{2})
       (count)
       (- (count (flatten rows)))))

(defn count-till-bigger-or-equal
  [coll n]
  (-> coll
      (find-first #(>= % n))
      (->> (.indexOf coll))
      (+ 1)
      (split-at coll)
      first
      count))

(defn get-scenic-score
  [coll n]
  (if (< (count coll) 1)
    0
    (if (> n (apply max coll))
      (count coll)
      (count-till-bigger-or-equal coll n))))

(defn scenic-score
  [coll n]
  (let [current (nth coll n)
        before (reverse (take n coll))
        after (nthrest coll (+ n 1))]
    [(get-scenic-score before current)
     (get-scenic-score after current)]))

(defn apply-scenic-score
  [coll]
  (reduce
   (fn [[acc n] item]
     [(conj acc (scenic-score coll n)) (+ 1 n)])
   [[] 0]
   coll))

(defn scenic-score-matrix
  [coll]
  (reduce
   (fn [acc list]
     (conj acc (first (apply-scenic-score list))))
   []
   coll))

(def scenic-score-rows (scenic-score-matrix rows))
(def scenic-score-columns
  (scenic-score-matrix columns))

(def scenic-score-columns-transposed
  (apply map vector scenic-score-columns))

(def tree-with-highest-scenic-score
  (->> (flatten scenic-score-columns-transposed)
       (interleave (flatten scenic-score-rows))
       (partition 4)
       (map #(apply * %))
       (apply max)))
