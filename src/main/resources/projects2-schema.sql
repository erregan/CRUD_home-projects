DROP TABLE IF EXISTS material;
DROP TABLE IF EXISTS step;
DROP TABLE IF EXISTS projects2_category;
DROP TABLE IF EXISTS category;
DROP TABLE IF EXISTS projects2;

CREATE TABLE projects2 (
	projects2_id INT AUTO_INCREMENT NOT NULL,
	projects2_name VARCHAR(128) NOT NULL,	
	estimated_hours DECIMAL(7,2),
	actual_hours DECIMAL(7,2),
	difficulty INT NOT NULL,
	notes TEXT,
	PRIMARY KEY (projects2_id)
);

CREATE TABLE category (
	category_id INT AUTO_INCREMENT NOT NULL,
	category_name VARCHAR(128) NOT NULL,
	PRIMARY KEY (category_id)
);

CREATE TABLE projects2_category (
	projects2_id INT NOT NULL,
	category_id INT NOT NULL,
	FOREIGN KEY (projects2_id) REFERENCES projects2 (projects2_id) ON DELETE CASCADE,
	FOREIGN KEY (category_id) REFERENCES category (category_id) ON DELETE CASCADE,
	UNIQUE KEY (projects2_id, category_id)
);

CREATE TABLE step (
	step_id INT AUTO_INCREMENT NOT NULL,
	projects2_id INT NOT NULL,
	step_text TEXT NOT NULL,
	step_order INT NOT NULL,
	PRIMARY KEY (step_id),
	FOREIGN KEY (projects2_id) REFERENCES projects2 (projects2_id) ON DELETE CASCADE
);

CREATE TABLE material (
	material_id INT AUTO_INCREMENT NOT NULL,
	projects2_id INT NOT NULL,
	material_name VARCHAR(128) NOT NULL,	
	num_required INT NOT NULL,
	cost DECIMAL(7,2),
	PRIMARY KEY (material_id),
	FOREIGN KEY (projects2_id)REFERENCES projects2 (projects2_id) ON DELETE CASCADE
);

