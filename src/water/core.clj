(ns water.core)

(defrecord Position [x y])

(defrecord Point [value position])

(defrecord RelativePoint [value point direction])

(defrecord Node [self drainsTo])

(defn make-position [x y] (Position. x y) )

(defn make-neighbor [x y relation maxX maxY] 
	(case relation
		:north (if (<= 0 (- y 1) ) (make-position x (- y 1)))
		:south (if (>= maxY (+ y 1) ) (make-position x (+ y 1)))
		:east (if (>= maxX (+ x 1) ) (make-position (+ x 1) y))
		:west (if (<= 0 (- x 1) ) (make-position (- x 1) y))
		nil)
)

(defn make-point [x y relation maxX maxY grid] 
	(let [position (make-neighbor x y relation maxX maxY)] 
		(if (not (nil? position)) 
			(Point. (nth (nth grid (:y position)) (:x position)) 
					position)
		)
	)
)


(defn make-relative [x y relation maxX maxY grid]
	(let [relative (make-point x y relation maxX maxY grid)]
		(if (not (nil? relative))
			(RelativePoint. (:value relative) relative relation)
		)
	)
)

(defn to-map [relatives] (into {} (for [relative relatives] [(:direction relative) relative])))

; Need help to figure out a better way to do this
(defn filter-relatives [relatives]
	(if(contains? relatives :self)
		(relatives :self)
		(if(contains? relatives :north)
			(relatives :north)
			(if(contains? relatives :west)
				(relatives :west)
				(if(contains? relatives :east)
					(relatives :east)
					(relatives :south)
				)
			)
		)
	)
)

(defn drains-to? [x y grid maxX maxY] (let [relatives (sort-by :value (remove nil? (list 
											(make-relative x y :north maxX maxY grid) 
											(make-relative x y :south maxX maxY grid)
											(make-relative x y :east maxX maxY grid)
											(make-relative x y :west maxX maxY grid)
										    (RelativePoint. (nth (nth grid y) x) 
										    				(Point. (nth (nth grid y) x) (make-position x y)) :self ))))
										    minimum (:value (first relatives))
										    filtered-relatives (to-map (filter #(= (:value %) minimum) relatives))] 
	(:point (filter-relatives filtered-relatives))))

(defn make-node [value x y maxX maxY grid] (Node. (Point. value (make-position x y))
										   (drains-to? x y grid maxX maxY))
)

(defn make-nodes [drainage maxX maxY] (vec (for [y (range (+ maxY 1))] 
									  			(vec (for [x (range (+ 1 maxX))] 
									  				(make-node (nth (nth drainage y) x) x y maxX maxY drainage)))
									  		)))

(defn populate-drain [drainage] drainage)

(defn convert-map [drainage] (clojure.walk/prewalk-replace {1 "a" 2 "a"} drainage))