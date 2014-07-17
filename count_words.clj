(def pages ["Today, we’ll continue our discussion of how functional programming helps with parallelism by looking"
            "Something might be bothering you—a Wikipedia dump runs to around 40GiB. If count-words starts by"
            "Because take is only interested in the first ten elements of its sequence argu- ment, range only needs to gene"])

(defn word-frequencies [words]
  (reduce
   (fn [counts word]
     (assoc counts word (inc (get counts word 0))))
   {} words))


(frequencies '(one fine day))

(defn get-words [text] (re-seq #"\w+" text))

(get-words (first pages))

(frequencies(first (map #(get-words %) pages)))
(defn count-words-sequential [pages]
  (frequencies (mapcat #(get-words %) pages)))

(count-words-sequential pages)
(pmap #(frequencies (get-words %)) pages)


(defn count-words-parallel [pages]
  (reduce (partial merge-with +)
          (pmap #(frequencies (get-words %)) pages)))

(count-words-parallel pages)

(require '[clojure.core.reducers :as r])
(r/map (partial * 2) [1 2 3 4])
;; init reducer
(r/map #(frequencies (get-words %))
       pages)

;;
(into []
      (r/map #(frequencies (get-words %)) pages))
;; partitions pages into groups of x
;; runs pmap function over batches of pages which mapconcats all pages in each batch and counts words
;; reduce merges each batch back to single map result
(defn count-words-with-partition [pages]
  (reduce (partial merge-with +)
          (pmap count-words-sequential (partition-all 1 pages))))

(count-words-with-partition pages)

