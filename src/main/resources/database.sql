-- public.permissions definition

-- Drop table

-- DROP TABLE public.permissions;

CREATE TABLE public.permissions (
	id uuid NOT NULL,
	permission_name varchar(255) NOT NULL,
	description text NULL,
	CONSTRAINT permissions_permission_name_key UNIQUE (permission_name),
	CONSTRAINT permissions_pkey PRIMARY KEY (id)
);


-- public.question definition

-- Drop table

-- DROP TABLE public.question;

CREATE TABLE public.question (
	id uuid NOT NULL,
	question text NOT NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	question_type int4 NOT NULL DEFAULT 1,
	CONSTRAINT question_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX question_question_uindex ON public.question USING btree (question);


-- public.roles definition

-- Drop table

-- DROP TABLE public.roles;

CREATE TABLE public.roles (
	id uuid NOT NULL,
	"name" varchar(50) NOT NULL,
	CONSTRAINT roles_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX roles_name_uindex ON public.roles USING btree (name);


-- public.tags definition

-- Drop table

-- DROP TABLE public.tags;

CREATE TABLE public.tags (
	id uuid NOT NULL,
	"name" varchar(100) NULL,
	CONSTRAINT tag_name_unique UNIQUE (name),
	CONSTRAINT tag_pk PRIMARY KEY (id)
);


-- public.test definition

-- Drop table

-- DROP TABLE public.test;

CREATE TABLE public.test (
	id uuid NOT NULL,
	title text NOT NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	CONSTRAINT test_pk PRIMARY KEY (id)
);
CREATE UNIQUE INDEX test_title_uindex ON public.test USING btree (title);


-- public.users definition

-- Drop table

-- DROP TABLE public.users;

CREATE TABLE public.users (
	id uuid NOT NULL,
	email varchar(320) NOT NULL,
	username varchar(32) NOT NULL,
	"password" varchar(64) NOT NULL,
	bio text NULL,
	image varchar(250) NULL,
	created_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	"enable" bool NULL DEFAULT true,
	expired_time timestamp NULL,
	credentials_expired_time timestamp NULL,
	CONSTRAINT users_email_key UNIQUE (email),
	CONSTRAINT users_pkey PRIMARY KEY (id),
	CONSTRAINT users_unique UNIQUE (username)
);


-- public.answer definition

-- Drop table

-- DROP TABLE public.answer;

CREATE TABLE public.answers (
	id uuid NOT NULL,
	answer text NOT NULL,
	truth bool NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL,
	question_id uuid NOT NULL,
	CONSTRAINT answer_pk PRIMARY KEY (id),
	CONSTRAINT question_fk FOREIGN KEY (question_id) REFERENCES public.question(id)
);


-- public.articles definition

-- Drop table

-- DROP TABLE public.articles;

CREATE TABLE public.articles (
	id uuid NOT NULL,
	author_id uuid NOT NULL,
	title varchar(250) NOT NULL,
	description text NOT NULL,
	body text NOT NULL,
	created_at timestamp NOT NULL,
	updated_at timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
	search_vector tsvector NULL,
	CONSTRAINT articles_pkey PRIMARY KEY (id),
	CONSTRAINT articles_author_id_fkey FOREIGN KEY (author_id) REFERENCES public.users(id)
);
CREATE INDEX articles_author_id_idx ON public.articles USING btree (author_id);
CREATE INDEX articles_search_vector_idx ON public.articles USING gin (search_vector);


-- public."comments" definition

-- Drop table

-- DROP TABLE public."comments";

CREATE TABLE public."comments" (
	id uuid NOT NULL,
	article_id uuid NULL,
	author_id uuid NULL,
	body text NULL,
	created_at timestamp NULL,
	updated_at timestamp NULL,
	CONSTRAINT comment_pk PRIMARY KEY (id),
	CONSTRAINT comment_articles_fk FOREIGN KEY (article_id) REFERENCES public.articles(id),
	CONSTRAINT comment_users_fk FOREIGN KEY (author_id) REFERENCES public.users(id)
);


-- public.favorites definition

-- Drop table

-- DROP TABLE public.favorites;

CREATE TABLE public.favorites (
	article_id uuid NOT NULL,
	user_id uuid NOT NULL,
	created_at timestamp NULL,
	CONSTRAINT favorites_pk PRIMARY KEY (article_id, user_id),
	CONSTRAINT favorites_articles_fk FOREIGN KEY (article_id) REFERENCES public.articles(id),
	CONSTRAINT favorites_users_fk FOREIGN KEY (user_id) REFERENCES public.users(id)
);


-- public.follows definition

-- Drop table

-- DROP TABLE public.follows;

CREATE TABLE public.follows (
	follower_id uuid NOT NULL,
	followee_id uuid NOT NULL,
	created_at timestamp NULL,
	CONSTRAINT follows_pk PRIMARY KEY (followee_id, follower_id),
	CONSTRAINT followee_users_fk FOREIGN KEY (followee_id) REFERENCES public.users(id),
	CONSTRAINT follower_users_fk FOREIGN KEY (follower_id) REFERENCES public.users(id)
);
CREATE INDEX follows_followee_id_idx ON public.follows USING btree (followee_id);


-- public.practice definition

-- Drop table

-- DROP TABLE public.practice;

CREATE TABLE public.practice (
	id uuid NOT NULL,
	test_id uuid NULL,
	tester_id uuid NULL,
	score int4 NULL,
	created_at timestamp NOT NULL DEFAULT now(),
	CONSTRAINT practice_pk PRIMARY KEY (id),
	CONSTRAINT practice___fk_test FOREIGN KEY (test_id) REFERENCES public.test(id),
	CONSTRAINT practice___fk_tester FOREIGN KEY (tester_id) REFERENCES public.users(id)
);


-- public.practice_choices definition

-- Drop table

-- DROP TABLE public.practice_choices;

CREATE TABLE public.practice_choices (
	id uuid NOT NULL,
	practice_id uuid NULL,
	answer_id uuid NULL,
	CONSTRAINT practice_choices_pk PRIMARY KEY (id),
	CONSTRAINT practice_choices_answer_id_fk FOREIGN KEY (answer_id) REFERENCES public.answer(id),
	CONSTRAINT practice_choices_practice_id_fk FOREIGN KEY (practice_id) REFERENCES public.practice(id)
);


-- public.role_permissions definition

-- Drop table

-- DROP TABLE public.role_permissions;

CREATE TABLE public.role_permissions (
	role_id uuid NOT NULL,
	permission_id uuid NOT NULL,
	CONSTRAINT role_permissions_pkey PRIMARY KEY (role_id, permission_id),
	CONSTRAINT role_permissions_permission_id_fkey FOREIGN KEY (permission_id) REFERENCES public.permissions(id),
	CONSTRAINT role_permissions_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id)
);


-- public.test_question definition

-- Drop table

-- DROP TABLE public.test_question;

CREATE TABLE public.test_question (
	test_id uuid NOT NULL,
	question_id uuid NOT NULL,
	CONSTRAINT test_question_pk PRIMARY KEY (test_id, question_id),
	CONSTRAINT question_fk FOREIGN KEY (question_id) REFERENCES public.question(id),
	CONSTRAINT test_fk FOREIGN KEY (test_id) REFERENCES public.test(id)
);


-- public.user_role definition

-- Drop table

-- DROP TABLE public.user_role;

CREATE TABLE public.user_roles (
	role_id uuid NOT NULL,
	user_id uuid NOT NULL,
	CONSTRAINT user_role_pk PRIMARY KEY (role_id, user_id),
	CONSTRAINT role___fk FOREIGN KEY (role_id) REFERENCES public.roles(id),
	CONSTRAINT user___fk FOREIGN KEY (user_id) REFERENCES public.users(id)
);
CREATE INDEX user_role_user__index ON public.user_role USING btree (user_id);


-- public.article_tag definition

-- Drop table

-- DROP TABLE public.article_tag;

CREATE TABLE public.article_tag (
	article_id uuid NOT NULL,
	tag_id uuid NOT NULL,
	CONSTRAINT article_tag_pk PRIMARY KEY (article_id, tag_id),
	CONSTRAINT article_tag_articles_fk FOREIGN KEY (article_id) REFERENCES public.articles(id),
	CONSTRAINT article_tag_tag_null_fk FOREIGN KEY (tag_id) REFERENCES public.tags(id)
);
CREATE INDEX article_tag_article_id_idx ON public.article_tag USING btree (article_id);
CREATE INDEX article_tag_idx_reverse ON public.article_tag USING btree (tag_id, article_id);
CREATE INDEX article_tag_tag_id_idx ON public.article_tag USING btree (tag_id);


-- public.essay_answer definition

-- Drop table

-- DROP TABLE public.essay_answer;

CREATE TABLE public.essay_answer (
	id uuid NOT NULL,
	answer text NULL,
	question_id uuid NOT NULL,
	created_at timestamp NOT NULL,
	practice_id uuid NOT NULL,
	CONSTRAINT essay_answer_pk PRIMARY KEY (id),
	CONSTRAINT essay_answer_practice_id_fk FOREIGN KEY (practice_id) REFERENCES public.practice(id),
	CONSTRAINT essay_answer_question_id_fk FOREIGN KEY (question_id) REFERENCES public.question(id)
);
CREATE UNIQUE INDEX essay_answer_question_id_practice_id_uindex ON public.essay_answer USING btree (question_id, practice_id);

INSERT INTO public.roles
(id, "name")
VALUES('56f9e88c-0066-4e3e-8793-119f6f2012d6'::uuid, 'ADMIN');
INSERT INTO public.roles
(id, "name")
VALUES('80e1e7af-0f80-4a5f-ab42-bfbfa6513da9'::uuid, 'USER');


CREATE TABLE public.refresh_token (
	id uuid primary key,
	refresh_token text unique,
    user_id uuid,
    CONSTRAINT refresh_token_fk FOREIGN KEY (user_id) REFERENCES public.users(id)
);