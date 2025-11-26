--
-- PostgreSQL database dump
--

\restrict YQtOAGJfyUp3ReEqvXOf2hjP5qaIVfNxmkPMXSf4oGyIFKmg5xpfqqKSS3vjEm7

-- Dumped from database version 15.14 (Debian 15.14-1.pgdg13+1)
-- Dumped by pg_dump version 15.14 (Debian 15.14-1.pgdg13+1)

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: admin_event_entity; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.admin_event_entity (
    id character varying(36) NOT NULL,
    admin_event_time bigint,
    realm_id character varying(255),
    operation_type character varying(255),
    auth_realm_id character varying(255),
    auth_client_id character varying(255),
    auth_user_id character varying(255),
    ip_address character varying(255),
    resource_path character varying(2550),
    representation text,
    error character varying(255),
    resource_type character varying(64)
);


ALTER TABLE public.admin_event_entity OWNER TO keycloak;

--
-- Name: associated_policy; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.associated_policy (
    policy_id character varying(36) NOT NULL,
    associated_policy_id character varying(36) NOT NULL
);


ALTER TABLE public.associated_policy OWNER TO keycloak;

--
-- Name: authentication_execution; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.authentication_execution (
    id character varying(36) NOT NULL,
    alias character varying(255),
    authenticator character varying(36),
    realm_id character varying(36),
    flow_id character varying(36),
    requirement integer,
    priority integer,
    authenticator_flow boolean DEFAULT false NOT NULL,
    auth_flow_id character varying(36),
    auth_config character varying(36)
);


ALTER TABLE public.authentication_execution OWNER TO keycloak;

--
-- Name: authentication_flow; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.authentication_flow (
    id character varying(36) NOT NULL,
    alias character varying(255),
    description character varying(255),
    realm_id character varying(36),
    provider_id character varying(36) DEFAULT 'basic-flow'::character varying NOT NULL,
    top_level boolean DEFAULT false NOT NULL,
    built_in boolean DEFAULT false NOT NULL
);


ALTER TABLE public.authentication_flow OWNER TO keycloak;

--
-- Name: authenticator_config; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.authenticator_config (
    id character varying(36) NOT NULL,
    alias character varying(255),
    realm_id character varying(36)
);


ALTER TABLE public.authenticator_config OWNER TO keycloak;

--
-- Name: authenticator_config_entry; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.authenticator_config_entry (
    authenticator_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.authenticator_config_entry OWNER TO keycloak;

--
-- Name: broker_link; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.broker_link (
    identity_provider character varying(255) NOT NULL,
    storage_provider_id character varying(255),
    realm_id character varying(36) NOT NULL,
    broker_user_id character varying(255),
    broker_username character varying(255),
    token text,
    user_id character varying(255) NOT NULL
);


ALTER TABLE public.broker_link OWNER TO keycloak;

--
-- Name: client; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client (
    id character varying(36) NOT NULL,
    enabled boolean DEFAULT false NOT NULL,
    full_scope_allowed boolean DEFAULT false NOT NULL,
    client_id character varying(255),
    not_before integer,
    public_client boolean DEFAULT false NOT NULL,
    secret character varying(255),
    base_url character varying(255),
    bearer_only boolean DEFAULT false NOT NULL,
    management_url character varying(255),
    surrogate_auth_required boolean DEFAULT false NOT NULL,
    realm_id character varying(36),
    protocol character varying(255),
    node_rereg_timeout integer DEFAULT 0,
    frontchannel_logout boolean DEFAULT false NOT NULL,
    consent_required boolean DEFAULT false NOT NULL,
    name character varying(255),
    service_accounts_enabled boolean DEFAULT false NOT NULL,
    client_authenticator_type character varying(255),
    root_url character varying(255),
    description character varying(255),
    registration_token character varying(255),
    standard_flow_enabled boolean DEFAULT true NOT NULL,
    implicit_flow_enabled boolean DEFAULT false NOT NULL,
    direct_access_grants_enabled boolean DEFAULT false NOT NULL,
    always_display_in_console boolean DEFAULT false NOT NULL
);


ALTER TABLE public.client OWNER TO keycloak;

--
-- Name: client_attributes; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_attributes (
    client_id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value text
);


ALTER TABLE public.client_attributes OWNER TO keycloak;

--
-- Name: client_auth_flow_bindings; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_auth_flow_bindings (
    client_id character varying(36) NOT NULL,
    flow_id character varying(36),
    binding_name character varying(255) NOT NULL
);


ALTER TABLE public.client_auth_flow_bindings OWNER TO keycloak;

--
-- Name: client_initial_access; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_initial_access (
    id character varying(36) NOT NULL,
    realm_id character varying(36) NOT NULL,
    "timestamp" integer,
    expiration integer,
    count integer,
    remaining_count integer
);


ALTER TABLE public.client_initial_access OWNER TO keycloak;

--
-- Name: client_node_registrations; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_node_registrations (
    client_id character varying(36) NOT NULL,
    value integer,
    name character varying(255) NOT NULL
);


ALTER TABLE public.client_node_registrations OWNER TO keycloak;

--
-- Name: client_scope; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_scope (
    id character varying(36) NOT NULL,
    name character varying(255),
    realm_id character varying(36),
    description character varying(255),
    protocol character varying(255)
);


ALTER TABLE public.client_scope OWNER TO keycloak;

--
-- Name: client_scope_attributes; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_scope_attributes (
    scope_id character varying(36) NOT NULL,
    value character varying(2048),
    name character varying(255) NOT NULL
);


ALTER TABLE public.client_scope_attributes OWNER TO keycloak;

--
-- Name: client_scope_client; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_scope_client (
    client_id character varying(255) NOT NULL,
    scope_id character varying(255) NOT NULL,
    default_scope boolean DEFAULT false NOT NULL
);


ALTER TABLE public.client_scope_client OWNER TO keycloak;

--
-- Name: client_scope_role_mapping; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_scope_role_mapping (
    scope_id character varying(36) NOT NULL,
    role_id character varying(36) NOT NULL
);


ALTER TABLE public.client_scope_role_mapping OWNER TO keycloak;

--
-- Name: client_session; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_session (
    id character varying(36) NOT NULL,
    client_id character varying(36),
    redirect_uri character varying(255),
    state character varying(255),
    "timestamp" integer,
    session_id character varying(36),
    auth_method character varying(255),
    realm_id character varying(255),
    auth_user_id character varying(36),
    current_action character varying(36)
);


ALTER TABLE public.client_session OWNER TO keycloak;

--
-- Name: client_session_auth_status; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_session_auth_status (
    authenticator character varying(36) NOT NULL,
    status integer,
    client_session character varying(36) NOT NULL
);


ALTER TABLE public.client_session_auth_status OWNER TO keycloak;

--
-- Name: client_session_note; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_session_note (
    name character varying(255) NOT NULL,
    value character varying(255),
    client_session character varying(36) NOT NULL
);


ALTER TABLE public.client_session_note OWNER TO keycloak;

--
-- Name: client_session_prot_mapper; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_session_prot_mapper (
    protocol_mapper_id character varying(36) NOT NULL,
    client_session character varying(36) NOT NULL
);


ALTER TABLE public.client_session_prot_mapper OWNER TO keycloak;

--
-- Name: client_session_role; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_session_role (
    role_id character varying(255) NOT NULL,
    client_session character varying(36) NOT NULL
);


ALTER TABLE public.client_session_role OWNER TO keycloak;

--
-- Name: client_user_session_note; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.client_user_session_note (
    name character varying(255) NOT NULL,
    value character varying(2048),
    client_session character varying(36) NOT NULL
);


ALTER TABLE public.client_user_session_note OWNER TO keycloak;

--
-- Name: component; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.component (
    id character varying(36) NOT NULL,
    name character varying(255),
    parent_id character varying(36),
    provider_id character varying(36),
    provider_type character varying(255),
    realm_id character varying(36),
    sub_type character varying(255)
);


ALTER TABLE public.component OWNER TO keycloak;

--
-- Name: component_config; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.component_config (
    id character varying(36) NOT NULL,
    component_id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value text
);


ALTER TABLE public.component_config OWNER TO keycloak;

--
-- Name: composite_role; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.composite_role (
    composite character varying(36) NOT NULL,
    child_role character varying(36) NOT NULL
);


ALTER TABLE public.composite_role OWNER TO keycloak;

--
-- Name: credential; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.credential (
    id character varying(36) NOT NULL,
    salt bytea,
    type character varying(255),
    user_id character varying(36),
    created_date bigint,
    user_label character varying(255),
    secret_data text,
    credential_data text,
    priority integer
);


ALTER TABLE public.credential OWNER TO keycloak;

--
-- Name: databasechangelog; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.databasechangelog (
    id character varying(255) NOT NULL,
    author character varying(255) NOT NULL,
    filename character varying(255) NOT NULL,
    dateexecuted timestamp without time zone NOT NULL,
    orderexecuted integer NOT NULL,
    exectype character varying(10) NOT NULL,
    md5sum character varying(35),
    description character varying(255),
    comments character varying(255),
    tag character varying(255),
    liquibase character varying(20),
    contexts character varying(255),
    labels character varying(255),
    deployment_id character varying(10)
);


ALTER TABLE public.databasechangelog OWNER TO keycloak;

--
-- Name: databasechangeloglock; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.databasechangeloglock (
    id integer NOT NULL,
    locked boolean NOT NULL,
    lockgranted timestamp without time zone,
    lockedby character varying(255)
);


ALTER TABLE public.databasechangeloglock OWNER TO keycloak;

--
-- Name: default_client_scope; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.default_client_scope (
    realm_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL,
    default_scope boolean DEFAULT false NOT NULL
);


ALTER TABLE public.default_client_scope OWNER TO keycloak;

--
-- Name: event_entity; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.event_entity (
    id character varying(36) NOT NULL,
    client_id character varying(255),
    details_json character varying(2550),
    error character varying(255),
    ip_address character varying(255),
    realm_id character varying(255),
    session_id character varying(255),
    event_time bigint,
    type character varying(255),
    user_id character varying(255),
    details_json_long_value text
);


ALTER TABLE public.event_entity OWNER TO keycloak;

--
-- Name: fed_user_attribute; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.fed_user_attribute (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36),
    value character varying(2024)
);


ALTER TABLE public.fed_user_attribute OWNER TO keycloak;

--
-- Name: fed_user_consent; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.fed_user_consent (
    id character varying(36) NOT NULL,
    client_id character varying(255),
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36),
    created_date bigint,
    last_updated_date bigint,
    client_storage_provider character varying(36),
    external_client_id character varying(255)
);


ALTER TABLE public.fed_user_consent OWNER TO keycloak;

--
-- Name: fed_user_consent_cl_scope; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.fed_user_consent_cl_scope (
    user_consent_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL
);


ALTER TABLE public.fed_user_consent_cl_scope OWNER TO keycloak;

--
-- Name: fed_user_credential; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.fed_user_credential (
    id character varying(36) NOT NULL,
    salt bytea,
    type character varying(255),
    created_date bigint,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36),
    user_label character varying(255),
    secret_data text,
    credential_data text,
    priority integer
);


ALTER TABLE public.fed_user_credential OWNER TO keycloak;

--
-- Name: fed_user_group_membership; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.fed_user_group_membership (
    group_id character varying(36) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36)
);


ALTER TABLE public.fed_user_group_membership OWNER TO keycloak;

--
-- Name: fed_user_required_action; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.fed_user_required_action (
    required_action character varying(255) DEFAULT ' '::character varying NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36)
);


ALTER TABLE public.fed_user_required_action OWNER TO keycloak;

--
-- Name: fed_user_role_mapping; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.fed_user_role_mapping (
    role_id character varying(36) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    storage_provider_id character varying(36)
);


ALTER TABLE public.fed_user_role_mapping OWNER TO keycloak;

--
-- Name: federated_identity; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.federated_identity (
    identity_provider character varying(255) NOT NULL,
    realm_id character varying(36),
    federated_user_id character varying(255),
    federated_username character varying(255),
    token text,
    user_id character varying(36) NOT NULL
);


ALTER TABLE public.federated_identity OWNER TO keycloak;

--
-- Name: federated_user; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.federated_user (
    id character varying(255) NOT NULL,
    storage_provider_id character varying(255),
    realm_id character varying(36) NOT NULL
);


ALTER TABLE public.federated_user OWNER TO keycloak;

--
-- Name: group_attribute; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.group_attribute (
    id character varying(36) DEFAULT 'sybase-needs-something-here'::character varying NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255),
    group_id character varying(36) NOT NULL
);


ALTER TABLE public.group_attribute OWNER TO keycloak;

--
-- Name: group_role_mapping; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.group_role_mapping (
    role_id character varying(36) NOT NULL,
    group_id character varying(36) NOT NULL
);


ALTER TABLE public.group_role_mapping OWNER TO keycloak;

--
-- Name: identity_provider; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.identity_provider (
    internal_id character varying(36) NOT NULL,
    enabled boolean DEFAULT false NOT NULL,
    provider_alias character varying(255),
    provider_id character varying(255),
    store_token boolean DEFAULT false NOT NULL,
    authenticate_by_default boolean DEFAULT false NOT NULL,
    realm_id character varying(36),
    add_token_role boolean DEFAULT true NOT NULL,
    trust_email boolean DEFAULT false NOT NULL,
    first_broker_login_flow_id character varying(36),
    post_broker_login_flow_id character varying(36),
    provider_display_name character varying(255),
    link_only boolean DEFAULT false NOT NULL
);


ALTER TABLE public.identity_provider OWNER TO keycloak;

--
-- Name: identity_provider_config; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.identity_provider_config (
    identity_provider_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.identity_provider_config OWNER TO keycloak;

--
-- Name: identity_provider_mapper; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.identity_provider_mapper (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    idp_alias character varying(255) NOT NULL,
    idp_mapper_name character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL
);


ALTER TABLE public.identity_provider_mapper OWNER TO keycloak;

--
-- Name: idp_mapper_config; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.idp_mapper_config (
    idp_mapper_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.idp_mapper_config OWNER TO keycloak;

--
-- Name: keycloak_group; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.keycloak_group (
    id character varying(36) NOT NULL,
    name character varying(255),
    parent_group character varying(36) NOT NULL,
    realm_id character varying(36)
);


ALTER TABLE public.keycloak_group OWNER TO keycloak;

--
-- Name: keycloak_role; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.keycloak_role (
    id character varying(36) NOT NULL,
    client_realm_constraint character varying(255),
    client_role boolean DEFAULT false NOT NULL,
    description character varying(255),
    name character varying(255),
    realm_id character varying(255),
    client character varying(36),
    realm character varying(36)
);


ALTER TABLE public.keycloak_role OWNER TO keycloak;

--
-- Name: migration_model; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.migration_model (
    id character varying(36) NOT NULL,
    version character varying(36),
    update_time bigint DEFAULT 0 NOT NULL
);


ALTER TABLE public.migration_model OWNER TO keycloak;

--
-- Name: offline_client_session; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.offline_client_session (
    user_session_id character varying(36) NOT NULL,
    client_id character varying(255) NOT NULL,
    offline_flag character varying(4) NOT NULL,
    "timestamp" integer,
    data text,
    client_storage_provider character varying(36) DEFAULT 'local'::character varying NOT NULL,
    external_client_id character varying(255) DEFAULT 'local'::character varying NOT NULL
);


ALTER TABLE public.offline_client_session OWNER TO keycloak;

--
-- Name: offline_user_session; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.offline_user_session (
    user_session_id character varying(36) NOT NULL,
    user_id character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    created_on integer NOT NULL,
    offline_flag character varying(4) NOT NULL,
    data text,
    last_session_refresh integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.offline_user_session OWNER TO keycloak;

--
-- Name: policy_config; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.policy_config (
    policy_id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value text
);


ALTER TABLE public.policy_config OWNER TO keycloak;

--
-- Name: protocol_mapper; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.protocol_mapper (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    protocol character varying(255) NOT NULL,
    protocol_mapper_name character varying(255) NOT NULL,
    client_id character varying(36),
    client_scope_id character varying(36)
);


ALTER TABLE public.protocol_mapper OWNER TO keycloak;

--
-- Name: protocol_mapper_config; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.protocol_mapper_config (
    protocol_mapper_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.protocol_mapper_config OWNER TO keycloak;

--
-- Name: realm; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.realm (
    id character varying(36) NOT NULL,
    access_code_lifespan integer,
    user_action_lifespan integer,
    access_token_lifespan integer,
    account_theme character varying(255),
    admin_theme character varying(255),
    email_theme character varying(255),
    enabled boolean DEFAULT false NOT NULL,
    events_enabled boolean DEFAULT false NOT NULL,
    events_expiration bigint,
    login_theme character varying(255),
    name character varying(255),
    not_before integer,
    password_policy character varying(2550),
    registration_allowed boolean DEFAULT false NOT NULL,
    remember_me boolean DEFAULT false NOT NULL,
    reset_password_allowed boolean DEFAULT false NOT NULL,
    social boolean DEFAULT false NOT NULL,
    ssl_required character varying(255),
    sso_idle_timeout integer,
    sso_max_lifespan integer,
    update_profile_on_soc_login boolean DEFAULT false NOT NULL,
    verify_email boolean DEFAULT false NOT NULL,
    master_admin_client character varying(36),
    login_lifespan integer,
    internationalization_enabled boolean DEFAULT false NOT NULL,
    default_locale character varying(255),
    reg_email_as_username boolean DEFAULT false NOT NULL,
    admin_events_enabled boolean DEFAULT false NOT NULL,
    admin_events_details_enabled boolean DEFAULT false NOT NULL,
    edit_username_allowed boolean DEFAULT false NOT NULL,
    otp_policy_counter integer DEFAULT 0,
    otp_policy_window integer DEFAULT 1,
    otp_policy_period integer DEFAULT 30,
    otp_policy_digits integer DEFAULT 6,
    otp_policy_alg character varying(36) DEFAULT 'HmacSHA1'::character varying,
    otp_policy_type character varying(36) DEFAULT 'totp'::character varying,
    browser_flow character varying(36),
    registration_flow character varying(36),
    direct_grant_flow character varying(36),
    reset_credentials_flow character varying(36),
    client_auth_flow character varying(36),
    offline_session_idle_timeout integer DEFAULT 0,
    revoke_refresh_token boolean DEFAULT false NOT NULL,
    access_token_life_implicit integer DEFAULT 0,
    login_with_email_allowed boolean DEFAULT true NOT NULL,
    duplicate_emails_allowed boolean DEFAULT false NOT NULL,
    docker_auth_flow character varying(36),
    refresh_token_max_reuse integer DEFAULT 0,
    allow_user_managed_access boolean DEFAULT false NOT NULL,
    sso_max_lifespan_remember_me integer DEFAULT 0 NOT NULL,
    sso_idle_timeout_remember_me integer DEFAULT 0 NOT NULL,
    default_role character varying(255)
);


ALTER TABLE public.realm OWNER TO keycloak;

--
-- Name: realm_attribute; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.realm_attribute (
    name character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL,
    value text
);


ALTER TABLE public.realm_attribute OWNER TO keycloak;

--
-- Name: realm_default_groups; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.realm_default_groups (
    realm_id character varying(36) NOT NULL,
    group_id character varying(36) NOT NULL
);


ALTER TABLE public.realm_default_groups OWNER TO keycloak;

--
-- Name: realm_enabled_event_types; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.realm_enabled_event_types (
    realm_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.realm_enabled_event_types OWNER TO keycloak;

--
-- Name: realm_events_listeners; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.realm_events_listeners (
    realm_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.realm_events_listeners OWNER TO keycloak;

--
-- Name: realm_localizations; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.realm_localizations (
    realm_id character varying(255) NOT NULL,
    locale character varying(255) NOT NULL,
    texts text NOT NULL
);


ALTER TABLE public.realm_localizations OWNER TO keycloak;

--
-- Name: realm_required_credential; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.realm_required_credential (
    type character varying(255) NOT NULL,
    form_label character varying(255),
    input boolean DEFAULT false NOT NULL,
    secret boolean DEFAULT false NOT NULL,
    realm_id character varying(36) NOT NULL
);


ALTER TABLE public.realm_required_credential OWNER TO keycloak;

--
-- Name: realm_smtp_config; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.realm_smtp_config (
    realm_id character varying(36) NOT NULL,
    value character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE public.realm_smtp_config OWNER TO keycloak;

--
-- Name: realm_supported_locales; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.realm_supported_locales (
    realm_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.realm_supported_locales OWNER TO keycloak;

--
-- Name: redirect_uris; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.redirect_uris (
    client_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.redirect_uris OWNER TO keycloak;

--
-- Name: required_action_config; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.required_action_config (
    required_action_id character varying(36) NOT NULL,
    value text,
    name character varying(255) NOT NULL
);


ALTER TABLE public.required_action_config OWNER TO keycloak;

--
-- Name: required_action_provider; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.required_action_provider (
    id character varying(36) NOT NULL,
    alias character varying(255),
    name character varying(255),
    realm_id character varying(36),
    enabled boolean DEFAULT false NOT NULL,
    default_action boolean DEFAULT false NOT NULL,
    provider_id character varying(255),
    priority integer
);


ALTER TABLE public.required_action_provider OWNER TO keycloak;

--
-- Name: resource_attribute; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.resource_attribute (
    id character varying(36) DEFAULT 'sybase-needs-something-here'::character varying NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255),
    resource_id character varying(36) NOT NULL
);


ALTER TABLE public.resource_attribute OWNER TO keycloak;

--
-- Name: resource_policy; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.resource_policy (
    resource_id character varying(36) NOT NULL,
    policy_id character varying(36) NOT NULL
);


ALTER TABLE public.resource_policy OWNER TO keycloak;

--
-- Name: resource_scope; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.resource_scope (
    resource_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL
);


ALTER TABLE public.resource_scope OWNER TO keycloak;

--
-- Name: resource_server; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.resource_server (
    id character varying(36) NOT NULL,
    allow_rs_remote_mgmt boolean DEFAULT false NOT NULL,
    policy_enforce_mode smallint NOT NULL,
    decision_strategy smallint DEFAULT 1 NOT NULL
);


ALTER TABLE public.resource_server OWNER TO keycloak;

--
-- Name: resource_server_perm_ticket; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.resource_server_perm_ticket (
    id character varying(36) NOT NULL,
    owner character varying(255) NOT NULL,
    requester character varying(255) NOT NULL,
    created_timestamp bigint NOT NULL,
    granted_timestamp bigint,
    resource_id character varying(36) NOT NULL,
    scope_id character varying(36),
    resource_server_id character varying(36) NOT NULL,
    policy_id character varying(36)
);


ALTER TABLE public.resource_server_perm_ticket OWNER TO keycloak;

--
-- Name: resource_server_policy; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.resource_server_policy (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    description character varying(255),
    type character varying(255) NOT NULL,
    decision_strategy smallint,
    logic smallint,
    resource_server_id character varying(36) NOT NULL,
    owner character varying(255)
);


ALTER TABLE public.resource_server_policy OWNER TO keycloak;

--
-- Name: resource_server_resource; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.resource_server_resource (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    type character varying(255),
    icon_uri character varying(255),
    owner character varying(255) NOT NULL,
    resource_server_id character varying(36) NOT NULL,
    owner_managed_access boolean DEFAULT false NOT NULL,
    display_name character varying(255)
);


ALTER TABLE public.resource_server_resource OWNER TO keycloak;

--
-- Name: resource_server_scope; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.resource_server_scope (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    icon_uri character varying(255),
    resource_server_id character varying(36) NOT NULL,
    display_name character varying(255)
);


ALTER TABLE public.resource_server_scope OWNER TO keycloak;

--
-- Name: resource_uris; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.resource_uris (
    resource_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.resource_uris OWNER TO keycloak;

--
-- Name: role_attribute; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.role_attribute (
    id character varying(36) NOT NULL,
    role_id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(255)
);


ALTER TABLE public.role_attribute OWNER TO keycloak;

--
-- Name: scope_mapping; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.scope_mapping (
    client_id character varying(36) NOT NULL,
    role_id character varying(36) NOT NULL
);


ALTER TABLE public.scope_mapping OWNER TO keycloak;

--
-- Name: scope_policy; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.scope_policy (
    scope_id character varying(36) NOT NULL,
    policy_id character varying(36) NOT NULL
);


ALTER TABLE public.scope_policy OWNER TO keycloak;

--
-- Name: user_attribute; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_attribute (
    name character varying(255) NOT NULL,
    value character varying(255),
    user_id character varying(36) NOT NULL,
    id character varying(36) DEFAULT 'sybase-needs-something-here'::character varying NOT NULL
);


ALTER TABLE public.user_attribute OWNER TO keycloak;

--
-- Name: user_consent; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_consent (
    id character varying(36) NOT NULL,
    client_id character varying(255),
    user_id character varying(36) NOT NULL,
    created_date bigint,
    last_updated_date bigint,
    client_storage_provider character varying(36),
    external_client_id character varying(255)
);


ALTER TABLE public.user_consent OWNER TO keycloak;

--
-- Name: user_consent_client_scope; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_consent_client_scope (
    user_consent_id character varying(36) NOT NULL,
    scope_id character varying(36) NOT NULL
);


ALTER TABLE public.user_consent_client_scope OWNER TO keycloak;

--
-- Name: user_entity; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_entity (
    id character varying(36) NOT NULL,
    email character varying(255),
    email_constraint character varying(255),
    email_verified boolean DEFAULT false NOT NULL,
    enabled boolean DEFAULT false NOT NULL,
    federation_link character varying(255),
    first_name character varying(255),
    last_name character varying(255),
    realm_id character varying(255),
    username character varying(255),
    created_timestamp bigint,
    service_account_client_link character varying(255),
    not_before integer DEFAULT 0 NOT NULL
);


ALTER TABLE public.user_entity OWNER TO keycloak;

--
-- Name: user_federation_config; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_federation_config (
    user_federation_provider_id character varying(36) NOT NULL,
    value character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE public.user_federation_config OWNER TO keycloak;

--
-- Name: user_federation_mapper; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_federation_mapper (
    id character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    federation_provider_id character varying(36) NOT NULL,
    federation_mapper_type character varying(255) NOT NULL,
    realm_id character varying(36) NOT NULL
);


ALTER TABLE public.user_federation_mapper OWNER TO keycloak;

--
-- Name: user_federation_mapper_config; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_federation_mapper_config (
    user_federation_mapper_id character varying(36) NOT NULL,
    value character varying(255),
    name character varying(255) NOT NULL
);


ALTER TABLE public.user_federation_mapper_config OWNER TO keycloak;

--
-- Name: user_federation_provider; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_federation_provider (
    id character varying(36) NOT NULL,
    changed_sync_period integer,
    display_name character varying(255),
    full_sync_period integer,
    last_sync integer,
    priority integer,
    provider_name character varying(255),
    realm_id character varying(36)
);


ALTER TABLE public.user_federation_provider OWNER TO keycloak;

--
-- Name: user_group_membership; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_group_membership (
    group_id character varying(36) NOT NULL,
    user_id character varying(36) NOT NULL
);


ALTER TABLE public.user_group_membership OWNER TO keycloak;

--
-- Name: user_required_action; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_required_action (
    user_id character varying(36) NOT NULL,
    required_action character varying(255) DEFAULT ' '::character varying NOT NULL
);


ALTER TABLE public.user_required_action OWNER TO keycloak;

--
-- Name: user_role_mapping; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_role_mapping (
    role_id character varying(255) NOT NULL,
    user_id character varying(36) NOT NULL
);


ALTER TABLE public.user_role_mapping OWNER TO keycloak;

--
-- Name: user_session; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_session (
    id character varying(36) NOT NULL,
    auth_method character varying(255),
    ip_address character varying(255),
    last_session_refresh integer,
    login_username character varying(255),
    realm_id character varying(255),
    remember_me boolean DEFAULT false NOT NULL,
    started integer,
    user_id character varying(255),
    user_session_state integer,
    broker_session_id character varying(255),
    broker_user_id character varying(255)
);


ALTER TABLE public.user_session OWNER TO keycloak;

--
-- Name: user_session_note; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.user_session_note (
    user_session character varying(36) NOT NULL,
    name character varying(255) NOT NULL,
    value character varying(2048)
);


ALTER TABLE public.user_session_note OWNER TO keycloak;

--
-- Name: username_login_failure; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.username_login_failure (
    realm_id character varying(36) NOT NULL,
    username character varying(255) NOT NULL,
    failed_login_not_before integer,
    last_failure bigint,
    last_ip_failure character varying(255),
    num_failures integer
);


ALTER TABLE public.username_login_failure OWNER TO keycloak;

--
-- Name: web_origins; Type: TABLE; Schema: public; Owner: keycloak
--

CREATE TABLE public.web_origins (
    client_id character varying(36) NOT NULL,
    value character varying(255) NOT NULL
);


ALTER TABLE public.web_origins OWNER TO keycloak;

--
-- Data for Name: admin_event_entity; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.admin_event_entity (id, admin_event_time, realm_id, operation_type, auth_realm_id, auth_client_id, auth_user_id, ip_address, resource_path, representation, error, resource_type) FROM stdin;
92070816-9076-4de8-97da-ba7b93e752a3	1759047969436	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE	14fac002-cb23-4725-a783-9c18edf80bf3	decb93ba-5e1b-4e0d-b297-1b3f69532f47	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133	172.20.0.1	events/config	\N	\N	REALM
9498c2e8-91c7-4233-a381-86e38c4098f8	1759047972709	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE	14fac002-cb23-4725-a783-9c18edf80bf3	decb93ba-5e1b-4e0d-b297-1b3f69532f47	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133	172.20.0.1	\N	\N	\N	REALM
a3602f30-9148-4f18-a977-dcddcd0e4f27	1759047972745	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE	14fac002-cb23-4725-a783-9c18edf80bf3	decb93ba-5e1b-4e0d-b297-1b3f69532f47	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133	172.20.0.1	events/config	\N	\N	REALM
49949ade-699d-4cac-ad00-cf1869312b12	1759048028590	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	DELETE	14fac002-cb23-4725-a783-9c18edf80bf3	decb93ba-5e1b-4e0d-b297-1b3f69532f47	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133	172.20.0.1	sessions/94f20132-281f-450b-a388-46e825946a26	\N	\N	USER_SESSION
ca59fa72-b2de-45f9-9691-6cf8c9f80c02	1759221253053	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE	14fac002-cb23-4725-a783-9c18edf80bf3	decb93ba-5e1b-4e0d-b297-1b3f69532f47	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133	172.20.0.1	\N	\N	\N	REALM
3937988d-5466-4c14-8785-3f601a71a549	1759222159191	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE	14fac002-cb23-4725-a783-9c18edf80bf3	decb93ba-5e1b-4e0d-b297-1b3f69532f47	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133	172.19.0.1	\N	\N	\N	REALM
\.


--
-- Data for Name: associated_policy; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.associated_policy (policy_id, associated_policy_id) FROM stdin;
\.


--
-- Data for Name: authentication_execution; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.authentication_execution (id, alias, authenticator, realm_id, flow_id, requirement, priority, authenticator_flow, auth_flow_id, auth_config) FROM stdin;
af07076c-f98b-43b1-904d-3e5084f05871	\N	auth-cookie	14fac002-cb23-4725-a783-9c18edf80bf3	41729bba-d3b9-48a9-9fcd-b1d69d84c4e3	2	10	f	\N	\N
42af244c-ecaa-4432-b7d0-6681e5304efe	\N	auth-spnego	14fac002-cb23-4725-a783-9c18edf80bf3	41729bba-d3b9-48a9-9fcd-b1d69d84c4e3	3	20	f	\N	\N
e7e8898c-cfb3-49ec-ab2e-250957b9aca2	\N	identity-provider-redirector	14fac002-cb23-4725-a783-9c18edf80bf3	41729bba-d3b9-48a9-9fcd-b1d69d84c4e3	2	25	f	\N	\N
46f2a691-1693-40dd-9511-f65fcc39b715	\N	\N	14fac002-cb23-4725-a783-9c18edf80bf3	41729bba-d3b9-48a9-9fcd-b1d69d84c4e3	2	30	t	50a41257-6699-474e-937b-0e9c9ab3b951	\N
4b221a66-533d-4f5d-b6a8-e6e48a6ccc9e	\N	auth-username-password-form	14fac002-cb23-4725-a783-9c18edf80bf3	50a41257-6699-474e-937b-0e9c9ab3b951	0	10	f	\N	\N
ce732f28-8b08-42db-b115-07155e1305eb	\N	\N	14fac002-cb23-4725-a783-9c18edf80bf3	50a41257-6699-474e-937b-0e9c9ab3b951	1	20	t	fab2785c-e5ea-4815-9c3d-bbe560986d1c	\N
6b684a84-aa48-46fe-ba0e-be87b46ef87f	\N	conditional-user-configured	14fac002-cb23-4725-a783-9c18edf80bf3	fab2785c-e5ea-4815-9c3d-bbe560986d1c	0	10	f	\N	\N
30f95f8f-ec08-4492-a985-03a8f93ece75	\N	auth-otp-form	14fac002-cb23-4725-a783-9c18edf80bf3	fab2785c-e5ea-4815-9c3d-bbe560986d1c	0	20	f	\N	\N
6c07b53d-5617-41a2-bb5c-e2beba7cfa59	\N	direct-grant-validate-username	14fac002-cb23-4725-a783-9c18edf80bf3	ad11a044-9a96-4f9d-a0f3-a7cd245a9e36	0	10	f	\N	\N
c3553b07-76b0-4afe-a19b-b1f132195a85	\N	direct-grant-validate-password	14fac002-cb23-4725-a783-9c18edf80bf3	ad11a044-9a96-4f9d-a0f3-a7cd245a9e36	0	20	f	\N	\N
2301b25c-43b7-4062-ba3a-b2079750f957	\N	\N	14fac002-cb23-4725-a783-9c18edf80bf3	ad11a044-9a96-4f9d-a0f3-a7cd245a9e36	1	30	t	1cc7ebb9-b029-4d32-aa74-4ff53dafc87c	\N
82e7ecf1-3fd5-4d4c-a58e-095c09744df0	\N	conditional-user-configured	14fac002-cb23-4725-a783-9c18edf80bf3	1cc7ebb9-b029-4d32-aa74-4ff53dafc87c	0	10	f	\N	\N
d3b9327c-d51e-4fed-b040-a53aa74f9a1f	\N	direct-grant-validate-otp	14fac002-cb23-4725-a783-9c18edf80bf3	1cc7ebb9-b029-4d32-aa74-4ff53dafc87c	0	20	f	\N	\N
f47acf85-2a7b-4229-b74f-b62099858091	\N	registration-page-form	14fac002-cb23-4725-a783-9c18edf80bf3	59436292-2219-4fb5-9116-172c24b3de06	0	10	t	cce4e5c2-007a-4168-b49f-8693e9038ac6	\N
8d256700-bfab-47d9-9827-94b267401421	\N	registration-user-creation	14fac002-cb23-4725-a783-9c18edf80bf3	cce4e5c2-007a-4168-b49f-8693e9038ac6	0	20	f	\N	\N
7a71593c-7e36-4151-a501-d950263046ce	\N	registration-password-action	14fac002-cb23-4725-a783-9c18edf80bf3	cce4e5c2-007a-4168-b49f-8693e9038ac6	0	50	f	\N	\N
ab6a708f-df9f-44c6-9712-ef0f9c4cc6e1	\N	registration-recaptcha-action	14fac002-cb23-4725-a783-9c18edf80bf3	cce4e5c2-007a-4168-b49f-8693e9038ac6	3	60	f	\N	\N
b74c2644-0dae-4b0b-80d8-95c23b9c2ec2	\N	registration-terms-and-conditions	14fac002-cb23-4725-a783-9c18edf80bf3	cce4e5c2-007a-4168-b49f-8693e9038ac6	3	70	f	\N	\N
89bdbfa6-8119-4a37-8254-40dcc6d38e1f	\N	reset-credentials-choose-user	14fac002-cb23-4725-a783-9c18edf80bf3	0d36c0be-cc19-4b48-ad42-8ceda3d03268	0	10	f	\N	\N
abfbba40-8e1e-4606-acc7-7f22be38176c	\N	reset-credential-email	14fac002-cb23-4725-a783-9c18edf80bf3	0d36c0be-cc19-4b48-ad42-8ceda3d03268	0	20	f	\N	\N
9f3ed970-a17f-41bb-93cc-e806b695f222	\N	reset-password	14fac002-cb23-4725-a783-9c18edf80bf3	0d36c0be-cc19-4b48-ad42-8ceda3d03268	0	30	f	\N	\N
088ae6bf-770b-4c09-843e-56d543a21577	\N	\N	14fac002-cb23-4725-a783-9c18edf80bf3	0d36c0be-cc19-4b48-ad42-8ceda3d03268	1	40	t	905792d7-6cbf-44d8-a172-b229202d19bd	\N
06080cf3-b878-4535-84f9-35a607c94ea8	\N	conditional-user-configured	14fac002-cb23-4725-a783-9c18edf80bf3	905792d7-6cbf-44d8-a172-b229202d19bd	0	10	f	\N	\N
cc283db2-991a-4f17-9c2f-8cdcbd1a1477	\N	reset-otp	14fac002-cb23-4725-a783-9c18edf80bf3	905792d7-6cbf-44d8-a172-b229202d19bd	0	20	f	\N	\N
49b94a64-e8d7-405a-afe2-a03c2b4d1b1d	\N	client-secret	14fac002-cb23-4725-a783-9c18edf80bf3	d480549e-ae7b-42d6-9f69-cd82a9037529	2	10	f	\N	\N
32955cc0-09f0-48a7-9785-5ac1a0e26da7	\N	client-jwt	14fac002-cb23-4725-a783-9c18edf80bf3	d480549e-ae7b-42d6-9f69-cd82a9037529	2	20	f	\N	\N
9825c006-481a-466d-9e76-1d3b3e45b219	\N	client-secret-jwt	14fac002-cb23-4725-a783-9c18edf80bf3	d480549e-ae7b-42d6-9f69-cd82a9037529	2	30	f	\N	\N
06c0696e-6c67-44f3-ab6e-54279f6a4c52	\N	client-x509	14fac002-cb23-4725-a783-9c18edf80bf3	d480549e-ae7b-42d6-9f69-cd82a9037529	2	40	f	\N	\N
664bbfff-ae22-4725-8443-48aa61def35f	\N	idp-review-profile	14fac002-cb23-4725-a783-9c18edf80bf3	0394af04-39c4-4024-9551-4e51cff527f6	0	10	f	\N	60b903e5-6f2f-4ada-927f-8da8d2692069
8d26abd5-e988-4de9-a757-ea3f34cbf5b8	\N	\N	14fac002-cb23-4725-a783-9c18edf80bf3	0394af04-39c4-4024-9551-4e51cff527f6	0	20	t	e414c4d6-a150-4e8d-a6e1-b9688f0989d9	\N
a00eb288-c06a-41c9-aaa3-d092e2f04d28	\N	idp-create-user-if-unique	14fac002-cb23-4725-a783-9c18edf80bf3	e414c4d6-a150-4e8d-a6e1-b9688f0989d9	2	10	f	\N	c14b446d-7fe2-4006-987f-2d825d0a37d1
98df731b-0d23-427d-9d2b-d7da5e38aa39	\N	\N	14fac002-cb23-4725-a783-9c18edf80bf3	e414c4d6-a150-4e8d-a6e1-b9688f0989d9	2	20	t	9ad2e6a8-ed10-4d76-8a00-043392a30b1e	\N
09b34945-b973-49a0-9774-71ac4577a06c	\N	idp-confirm-link	14fac002-cb23-4725-a783-9c18edf80bf3	9ad2e6a8-ed10-4d76-8a00-043392a30b1e	0	10	f	\N	\N
b90f47f5-d21c-4356-ac7d-e92362c117a4	\N	\N	14fac002-cb23-4725-a783-9c18edf80bf3	9ad2e6a8-ed10-4d76-8a00-043392a30b1e	0	20	t	ab6ef069-16e0-415f-91a5-31cf81c1f38f	\N
22eb6956-f845-4fa5-bfbf-5ac7072bbba5	\N	idp-email-verification	14fac002-cb23-4725-a783-9c18edf80bf3	ab6ef069-16e0-415f-91a5-31cf81c1f38f	2	10	f	\N	\N
7b6846a7-61b2-4681-8416-ab97593fc585	\N	\N	14fac002-cb23-4725-a783-9c18edf80bf3	ab6ef069-16e0-415f-91a5-31cf81c1f38f	2	20	t	0795fe86-34e3-4db7-a066-a98c53af24c0	\N
8a8183cf-2ca0-4e81-8ca2-45c8c02ddfa4	\N	idp-username-password-form	14fac002-cb23-4725-a783-9c18edf80bf3	0795fe86-34e3-4db7-a066-a98c53af24c0	0	10	f	\N	\N
2e31a164-6288-489e-8bbe-9437ea4a63a0	\N	\N	14fac002-cb23-4725-a783-9c18edf80bf3	0795fe86-34e3-4db7-a066-a98c53af24c0	1	20	t	6abf2467-5d4d-4b05-ba3f-572cca822926	\N
35f82726-8b36-40c3-bd51-45ccbe0078df	\N	conditional-user-configured	14fac002-cb23-4725-a783-9c18edf80bf3	6abf2467-5d4d-4b05-ba3f-572cca822926	0	10	f	\N	\N
59018015-e7f4-4ace-9e2a-0460d4350789	\N	auth-otp-form	14fac002-cb23-4725-a783-9c18edf80bf3	6abf2467-5d4d-4b05-ba3f-572cca822926	0	20	f	\N	\N
ebfe00e7-112c-42e5-b4b1-7150ad30e631	\N	http-basic-authenticator	14fac002-cb23-4725-a783-9c18edf80bf3	60c5ff36-7744-4333-a96c-611e93542be6	0	10	f	\N	\N
35fa8d02-a7d5-4b29-b2e2-7bd11f49b5e0	\N	docker-http-basic-authenticator	14fac002-cb23-4725-a783-9c18edf80bf3	719d6427-f8a9-48a9-b597-c58cec100714	0	10	f	\N	\N
42a5468c-57df-4470-b685-9355f6fafb10	\N	auth-cookie	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	e882a7e3-f4c6-4957-9413-0051eb35af41	2	10	f	\N	\N
65ec97dd-9888-4d4a-aea3-533db1fdb73c	\N	auth-spnego	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	e882a7e3-f4c6-4957-9413-0051eb35af41	3	20	f	\N	\N
21705f73-8877-4945-9fb1-88873c2dae4a	\N	identity-provider-redirector	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	e882a7e3-f4c6-4957-9413-0051eb35af41	2	25	f	\N	\N
43610a3c-f40d-4e8c-a0d7-e59a855ba60d	\N	\N	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	e882a7e3-f4c6-4957-9413-0051eb35af41	2	30	t	414481b9-28a2-4284-9a64-243ac864f99d	\N
a97377cc-2388-4bf9-a41c-c37f841c2d79	\N	auth-username-password-form	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	414481b9-28a2-4284-9a64-243ac864f99d	0	10	f	\N	\N
af208ce1-8e5a-49f0-bdf8-20046c5e86b6	\N	\N	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	414481b9-28a2-4284-9a64-243ac864f99d	1	20	t	eb265607-2600-4f2d-a144-cb765d9e4b54	\N
412efb1c-1f08-48cc-b9e6-9941b36d1857	\N	conditional-user-configured	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	eb265607-2600-4f2d-a144-cb765d9e4b54	0	10	f	\N	\N
c3b3620f-5407-4e71-97b4-e5ac133c5018	\N	auth-otp-form	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	eb265607-2600-4f2d-a144-cb765d9e4b54	0	20	f	\N	\N
ea3f5098-cbde-44c1-b7bb-4cea73a3e71d	\N	direct-grant-validate-username	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	42e05d04-47fd-46ac-8695-d2488426caad	0	10	f	\N	\N
aaa6f125-e45f-4eb2-88a3-33994e357ebd	\N	direct-grant-validate-password	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	42e05d04-47fd-46ac-8695-d2488426caad	0	20	f	\N	\N
3a1eacb5-5bdf-4e8b-8935-e0782f3a9234	\N	\N	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	42e05d04-47fd-46ac-8695-d2488426caad	1	30	t	3f27acc5-64a4-48e7-b7f4-8166155a3e59	\N
00dfd912-c277-46a9-906f-7b6048ae1a22	\N	conditional-user-configured	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	3f27acc5-64a4-48e7-b7f4-8166155a3e59	0	10	f	\N	\N
40d52d56-dda4-4197-8d6a-0a129131aa65	\N	direct-grant-validate-otp	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	3f27acc5-64a4-48e7-b7f4-8166155a3e59	0	20	f	\N	\N
23f91153-9e4a-4c61-aa5e-c86aa79751d1	\N	registration-page-form	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	7d461827-11cd-4218-ac62-21db69cb5cb4	0	10	t	dd2754d1-92e7-44a7-965b-c3d1cc97b30d	\N
237afead-eef6-4c88-811f-2880279cc505	\N	registration-user-creation	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	dd2754d1-92e7-44a7-965b-c3d1cc97b30d	0	20	f	\N	\N
09359c55-747c-4ad7-b17f-7b48a9eb9c69	\N	registration-password-action	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	dd2754d1-92e7-44a7-965b-c3d1cc97b30d	0	50	f	\N	\N
b181ca81-bdbe-4569-a282-91b0e68f8538	\N	registration-recaptcha-action	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	dd2754d1-92e7-44a7-965b-c3d1cc97b30d	3	60	f	\N	\N
d5e77759-8fba-4a8f-a2df-a92ab5f91bfc	\N	reset-credentials-choose-user	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	cd278064-b405-49a9-b518-118680e4c780	0	10	f	\N	\N
a60600ed-670e-4df5-923a-57d61122b4e5	\N	reset-credential-email	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	cd278064-b405-49a9-b518-118680e4c780	0	20	f	\N	\N
a0a1d9c6-1e33-4986-bd3f-8547ce186d5e	\N	reset-password	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	cd278064-b405-49a9-b518-118680e4c780	0	30	f	\N	\N
f6d0055f-6bd2-4b4e-a918-c6bf90215326	\N	\N	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	cd278064-b405-49a9-b518-118680e4c780	1	40	t	c3950ae3-9a33-4dd0-97fd-704050fc0c2a	\N
b3670e9b-5aec-4bd4-ac28-6ac650d1b213	\N	conditional-user-configured	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	c3950ae3-9a33-4dd0-97fd-704050fc0c2a	0	10	f	\N	\N
621e42b1-d1c3-4ea9-94e7-7ee57a28ae40	\N	reset-otp	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	c3950ae3-9a33-4dd0-97fd-704050fc0c2a	0	20	f	\N	\N
0a82809f-fee0-405a-be28-0d6975f8b6d3	\N	client-secret	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	94324de8-45ab-4312-84e3-a0835c67d5fc	2	10	f	\N	\N
47e5921c-fd75-416a-840d-af42ad5f2bc6	\N	client-jwt	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	94324de8-45ab-4312-84e3-a0835c67d5fc	2	20	f	\N	\N
a8f8cfdc-444d-4a97-b336-9ae48fe502b1	\N	client-secret-jwt	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	94324de8-45ab-4312-84e3-a0835c67d5fc	2	30	f	\N	\N
c8d86d29-a4cb-47c1-ac3d-1e91df38c949	\N	client-x509	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	94324de8-45ab-4312-84e3-a0835c67d5fc	2	40	f	\N	\N
b9c490aa-fc60-4d86-8ae9-243f39b54610	\N	idp-review-profile	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	89687619-2b58-4e6b-b008-7b4200971980	0	10	f	\N	c457d368-2b5f-4bcf-a1d2-04b4866a5aa5
bf014653-1d2a-462e-a187-7f3b4e755c57	\N	\N	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	89687619-2b58-4e6b-b008-7b4200971980	0	20	t	7ebe2d0d-1212-4950-aabe-d11448555dc9	\N
eae266d0-e15f-489d-ae00-ebe4fb49a199	\N	idp-create-user-if-unique	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	7ebe2d0d-1212-4950-aabe-d11448555dc9	2	10	f	\N	4cfb272d-83bb-4f2d-ad71-d6fb41ecddd1
6796ed2b-f534-4084-9274-5114c0f10aa6	\N	\N	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	7ebe2d0d-1212-4950-aabe-d11448555dc9	2	20	t	105b1fcd-bee7-4b64-86b0-70323a1e06f1	\N
53f3d12b-2533-48cc-a834-ee2ddaf741a2	\N	idp-confirm-link	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	105b1fcd-bee7-4b64-86b0-70323a1e06f1	0	10	f	\N	\N
7c715995-7785-4e65-b000-557130b8fa3f	\N	\N	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	105b1fcd-bee7-4b64-86b0-70323a1e06f1	0	20	t	288984e8-ab10-48f5-91da-e27ef09ea21c	\N
264ccbe2-6acf-4df4-8af7-800b2e825c4a	\N	idp-email-verification	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	288984e8-ab10-48f5-91da-e27ef09ea21c	2	10	f	\N	\N
c38e6007-d494-4a47-b47f-466af2c2b195	\N	\N	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	288984e8-ab10-48f5-91da-e27ef09ea21c	2	20	t	380c0ef0-ace1-4a7f-94fd-f2e8757322a9	\N
049a7feb-0c2d-4ff8-9bcc-1ffd8ad819fd	\N	idp-username-password-form	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	380c0ef0-ace1-4a7f-94fd-f2e8757322a9	0	10	f	\N	\N
7500b7ac-14f7-4b77-8c93-d71db30a86ec	\N	\N	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	380c0ef0-ace1-4a7f-94fd-f2e8757322a9	1	20	t	c29c6782-e742-49f8-89d5-c95e22786348	\N
97021d4f-2c50-49fc-8085-6675cc1c0d37	\N	conditional-user-configured	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	c29c6782-e742-49f8-89d5-c95e22786348	0	10	f	\N	\N
4a47264f-5471-4419-827e-04759dfcfc8f	\N	auth-otp-form	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	c29c6782-e742-49f8-89d5-c95e22786348	0	20	f	\N	\N
b16f313b-0ed0-4a68-ba4b-8a3f5f4b2937	\N	http-basic-authenticator	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	defc5d0c-39dc-46ca-8afb-4b2b94c90578	0	10	f	\N	\N
d4673130-d434-4851-b769-d5517b735f09	\N	docker-http-basic-authenticator	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	a0806bdb-8462-44b1-b8cd-4d5b51e9ed52	0	10	f	\N	\N
\.


--
-- Data for Name: authentication_flow; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.authentication_flow (id, alias, description, realm_id, provider_id, top_level, built_in) FROM stdin;
41729bba-d3b9-48a9-9fcd-b1d69d84c4e3	browser	browser based authentication	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	t	t
50a41257-6699-474e-937b-0e9c9ab3b951	forms	Username, password, otp and other auth forms.	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	f	t
fab2785c-e5ea-4815-9c3d-bbe560986d1c	Browser - Conditional OTP	Flow to determine if the OTP is required for the authentication	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	f	t
ad11a044-9a96-4f9d-a0f3-a7cd245a9e36	direct grant	OpenID Connect Resource Owner Grant	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	t	t
1cc7ebb9-b029-4d32-aa74-4ff53dafc87c	Direct Grant - Conditional OTP	Flow to determine if the OTP is required for the authentication	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	f	t
59436292-2219-4fb5-9116-172c24b3de06	registration	registration flow	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	t	t
cce4e5c2-007a-4168-b49f-8693e9038ac6	registration form	registration form	14fac002-cb23-4725-a783-9c18edf80bf3	form-flow	f	t
0d36c0be-cc19-4b48-ad42-8ceda3d03268	reset credentials	Reset credentials for a user if they forgot their password or something	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	t	t
905792d7-6cbf-44d8-a172-b229202d19bd	Reset - Conditional OTP	Flow to determine if the OTP should be reset or not. Set to REQUIRED to force.	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	f	t
d480549e-ae7b-42d6-9f69-cd82a9037529	clients	Base authentication for clients	14fac002-cb23-4725-a783-9c18edf80bf3	client-flow	t	t
0394af04-39c4-4024-9551-4e51cff527f6	first broker login	Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	t	t
e414c4d6-a150-4e8d-a6e1-b9688f0989d9	User creation or linking	Flow for the existing/non-existing user alternatives	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	f	t
9ad2e6a8-ed10-4d76-8a00-043392a30b1e	Handle Existing Account	Handle what to do if there is existing account with same email/username like authenticated identity provider	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	f	t
ab6ef069-16e0-415f-91a5-31cf81c1f38f	Account verification options	Method with which to verity the existing account	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	f	t
0795fe86-34e3-4db7-a066-a98c53af24c0	Verify Existing Account by Re-authentication	Reauthentication of existing account	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	f	t
6abf2467-5d4d-4b05-ba3f-572cca822926	First broker login - Conditional OTP	Flow to determine if the OTP is required for the authentication	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	f	t
60c5ff36-7744-4333-a96c-611e93542be6	saml ecp	SAML ECP Profile Authentication Flow	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	t	t
719d6427-f8a9-48a9-b597-c58cec100714	docker auth	Used by Docker clients to authenticate against the IDP	14fac002-cb23-4725-a783-9c18edf80bf3	basic-flow	t	t
e882a7e3-f4c6-4957-9413-0051eb35af41	browser	browser based authentication	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	t	t
414481b9-28a2-4284-9a64-243ac864f99d	forms	Username, password, otp and other auth forms.	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	f	t
eb265607-2600-4f2d-a144-cb765d9e4b54	Browser - Conditional OTP	Flow to determine if the OTP is required for the authentication	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	f	t
42e05d04-47fd-46ac-8695-d2488426caad	direct grant	OpenID Connect Resource Owner Grant	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	t	t
3f27acc5-64a4-48e7-b7f4-8166155a3e59	Direct Grant - Conditional OTP	Flow to determine if the OTP is required for the authentication	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	f	t
7d461827-11cd-4218-ac62-21db69cb5cb4	registration	registration flow	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	t	t
dd2754d1-92e7-44a7-965b-c3d1cc97b30d	registration form	registration form	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	form-flow	f	t
cd278064-b405-49a9-b518-118680e4c780	reset credentials	Reset credentials for a user if they forgot their password or something	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	t	t
c3950ae3-9a33-4dd0-97fd-704050fc0c2a	Reset - Conditional OTP	Flow to determine if the OTP should be reset or not. Set to REQUIRED to force.	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	f	t
94324de8-45ab-4312-84e3-a0835c67d5fc	clients	Base authentication for clients	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	client-flow	t	t
89687619-2b58-4e6b-b008-7b4200971980	first broker login	Actions taken after first broker login with identity provider account, which is not yet linked to any Keycloak account	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	t	t
7ebe2d0d-1212-4950-aabe-d11448555dc9	User creation or linking	Flow for the existing/non-existing user alternatives	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	f	t
105b1fcd-bee7-4b64-86b0-70323a1e06f1	Handle Existing Account	Handle what to do if there is existing account with same email/username like authenticated identity provider	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	f	t
288984e8-ab10-48f5-91da-e27ef09ea21c	Account verification options	Method with which to verity the existing account	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	f	t
380c0ef0-ace1-4a7f-94fd-f2e8757322a9	Verify Existing Account by Re-authentication	Reauthentication of existing account	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	f	t
c29c6782-e742-49f8-89d5-c95e22786348	First broker login - Conditional OTP	Flow to determine if the OTP is required for the authentication	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	f	t
defc5d0c-39dc-46ca-8afb-4b2b94c90578	saml ecp	SAML ECP Profile Authentication Flow	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	t	t
a0806bdb-8462-44b1-b8cd-4d5b51e9ed52	docker auth	Used by Docker clients to authenticate against the IDP	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	t	t
51e04df4-2869-4c16-b877-2439b08d4745	1		9d79aac4-eba0-4996-9b11-f9f8cf8b890a	basic-flow	t	f
\.


--
-- Data for Name: authenticator_config; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.authenticator_config (id, alias, realm_id) FROM stdin;
60b903e5-6f2f-4ada-927f-8da8d2692069	review profile config	14fac002-cb23-4725-a783-9c18edf80bf3
c14b446d-7fe2-4006-987f-2d825d0a37d1	create unique user config	14fac002-cb23-4725-a783-9c18edf80bf3
c457d368-2b5f-4bcf-a1d2-04b4866a5aa5	review profile config	9d79aac4-eba0-4996-9b11-f9f8cf8b890a
4cfb272d-83bb-4f2d-ad71-d6fb41ecddd1	create unique user config	9d79aac4-eba0-4996-9b11-f9f8cf8b890a
\.


--
-- Data for Name: authenticator_config_entry; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.authenticator_config_entry (authenticator_id, value, name) FROM stdin;
60b903e5-6f2f-4ada-927f-8da8d2692069	missing	update.profile.on.first.login
c14b446d-7fe2-4006-987f-2d825d0a37d1	false	require.password.update.after.registration
4cfb272d-83bb-4f2d-ad71-d6fb41ecddd1	false	require.password.update.after.registration
c457d368-2b5f-4bcf-a1d2-04b4866a5aa5	missing	update.profile.on.first.login
\.


--
-- Data for Name: broker_link; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.broker_link (identity_provider, storage_provider_id, realm_id, broker_user_id, broker_username, token, user_id) FROM stdin;
\.


--
-- Data for Name: client; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client (id, enabled, full_scope_allowed, client_id, not_before, public_client, secret, base_url, bearer_only, management_url, surrogate_auth_required, realm_id, protocol, node_rereg_timeout, frontchannel_logout, consent_required, name, service_accounts_enabled, client_authenticator_type, root_url, description, registration_token, standard_flow_enabled, implicit_flow_enabled, direct_access_grants_enabled, always_display_in_console) FROM stdin;
ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	f	master-realm	0	f	\N	\N	t	\N	f	14fac002-cb23-4725-a783-9c18edf80bf3	\N	0	f	f	master Realm	f	client-secret	\N	\N	\N	t	f	f	f
6959959b-71a5-48a6-862a-cee83b060340	t	f	account	0	t	\N	/realms/master/account/	f	\N	f	14fac002-cb23-4725-a783-9c18edf80bf3	openid-connect	0	f	f	${client_account}	f	client-secret	${authBaseUrl}	\N	\N	t	f	f	f
f9934ba3-18fc-459e-a566-40290c557df0	t	f	account-console	0	t	\N	/realms/master/account/	f	\N	f	14fac002-cb23-4725-a783-9c18edf80bf3	openid-connect	0	f	f	${client_account-console}	f	client-secret	${authBaseUrl}	\N	\N	t	f	f	f
5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	t	f	broker	0	f	\N	\N	t	\N	f	14fac002-cb23-4725-a783-9c18edf80bf3	openid-connect	0	f	f	${client_broker}	f	client-secret	\N	\N	\N	t	f	f	f
decb93ba-5e1b-4e0d-b297-1b3f69532f47	t	f	security-admin-console	0	t	\N	/admin/master/console/	f	\N	f	14fac002-cb23-4725-a783-9c18edf80bf3	openid-connect	0	f	f	${client_security-admin-console}	f	client-secret	${authAdminUrl}	\N	\N	t	f	f	f
47e58864-b732-4b3c-853b-8dcf9fec7838	t	f	admin-cli	0	t	\N	\N	f	\N	f	14fac002-cb23-4725-a783-9c18edf80bf3	openid-connect	0	f	f	${client_admin-cli}	f	client-secret	\N	\N	\N	f	f	t	f
d72824fb-285d-4a76-ac38-cd55d99064aa	t	f	ps-realm-realm	0	f	\N	\N	t	\N	f	14fac002-cb23-4725-a783-9c18edf80bf3	\N	0	f	f	ps-realm Realm	f	client-secret	\N	\N	\N	t	f	f	f
740e44d0-d37d-4bf6-a17e-31556b54fa34	t	f	realm-management	0	f	\N	\N	t	\N	f	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	openid-connect	0	f	f	${client_realm-management}	f	client-secret	\N	\N	\N	t	f	f	f
d935986d-48a9-4dfd-a6ef-215644092a3f	t	f	account	0	t	\N	/realms/ps-realm/account/	f	\N	f	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	openid-connect	0	f	f	${client_account}	f	client-secret	${authBaseUrl}	\N	\N	t	f	f	f
136f4d7c-de2f-40e0-ba23-c3183b70c007	t	f	account-console	0	t	\N	/realms/ps-realm/account/	f	\N	f	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	openid-connect	0	f	f	${client_account-console}	f	client-secret	${authBaseUrl}	\N	\N	t	f	f	f
05465146-c8ef-43c7-b824-29857a1e181e	t	f	broker	0	f	\N	\N	t	\N	f	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	openid-connect	0	f	f	${client_broker}	f	client-secret	\N	\N	\N	t	f	f	f
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	t	f	security-admin-console	0	t	\N	/admin/ps-realm/console/	f	\N	f	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	openid-connect	0	f	f	${client_security-admin-console}	f	client-secret	${authAdminUrl}	\N	\N	t	f	f	f
b5ba0829-4072-406d-a674-7fe4fd847617	t	f	admin-cli	0	t	\N	\N	f	\N	f	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	openid-connect	0	f	f	${client_admin-cli}	f	client-secret	\N	\N	\N	f	f	t	f
0f933f03-43e3-4b50-8a06-ecec138f9dcd	t	t	fanwei	0	f	REKQMO0YGRxP1ndrWC2M8DgMCePTN0Rt	http://localhost:3002/sso/home	f	http://localhost:3002/	f	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	openid-connect	-1	t	f		t	client-secret	http://localhost:3002/		b7931f0f-b71f-4f12-a0df-349476d3f012	t	f	t	f
572b0c4d-ce45-4197-9815-f797e44d25d5	t	t	ps-be	0	t	\N	http://localhost:8082/ps-be/api/sso/callback	f	\N	f	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	openid-connect	-1	f	f	ps-be	f	client-secret	\N		\N	t	f	t	f
3ad38726-150e-49ce-8e3d-a627624b9af5	t	t	tingche	0	t	\N	http://localhost:3001/home	f	http://localhost:3001/	f	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	openid-connect	-1	t	f		f	client-secret	http://localhost:3001/		\N	t	f	t	f
\.


--
-- Data for Name: client_attributes; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_attributes (client_id, name, value) FROM stdin;
6959959b-71a5-48a6-862a-cee83b060340	post.logout.redirect.uris	+
f9934ba3-18fc-459e-a566-40290c557df0	post.logout.redirect.uris	+
f9934ba3-18fc-459e-a566-40290c557df0	pkce.code.challenge.method	S256
decb93ba-5e1b-4e0d-b297-1b3f69532f47	post.logout.redirect.uris	+
decb93ba-5e1b-4e0d-b297-1b3f69532f47	pkce.code.challenge.method	S256
d935986d-48a9-4dfd-a6ef-215644092a3f	post.logout.redirect.uris	+
136f4d7c-de2f-40e0-ba23-c3183b70c007	post.logout.redirect.uris	+
136f4d7c-de2f-40e0-ba23-c3183b70c007	pkce.code.challenge.method	S256
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	post.logout.redirect.uris	+
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	pkce.code.challenge.method	S256
572b0c4d-ce45-4197-9815-f797e44d25d5	backchannel.logout.session.required	true
572b0c4d-ce45-4197-9815-f797e44d25d5	backchannel.logout.revoke.offline.tokens	false
3ad38726-150e-49ce-8e3d-a627624b9af5	oauth2.device.authorization.grant.enabled	false
3ad38726-150e-49ce-8e3d-a627624b9af5	oidc.ciba.grant.enabled	false
3ad38726-150e-49ce-8e3d-a627624b9af5	backchannel.logout.session.required	true
3ad38726-150e-49ce-8e3d-a627624b9af5	backchannel.logout.revoke.offline.tokens	false
3ad38726-150e-49ce-8e3d-a627624b9af5	display.on.consent.screen	false
3ad38726-150e-49ce-8e3d-a627624b9af5	use.refresh.tokens	true
3ad38726-150e-49ce-8e3d-a627624b9af5	client_credentials.use_refresh_token	false
3ad38726-150e-49ce-8e3d-a627624b9af5	token.response.type.bearer.lower-case	false
3ad38726-150e-49ce-8e3d-a627624b9af5	tls.client.certificate.bound.access.tokens	false
3ad38726-150e-49ce-8e3d-a627624b9af5	require.pushed.authorization.requests	false
3ad38726-150e-49ce-8e3d-a627624b9af5	acr.loa.map	{}
3ad38726-150e-49ce-8e3d-a627624b9af5	post.logout.redirect.uris	http://localhost:3001/sso/logout
0f933f03-43e3-4b50-8a06-ecec138f9dcd	client.secret.creation.time	1759042365
0f933f03-43e3-4b50-8a06-ecec138f9dcd	oauth2.device.authorization.grant.enabled	false
0f933f03-43e3-4b50-8a06-ecec138f9dcd	oidc.ciba.grant.enabled	false
0f933f03-43e3-4b50-8a06-ecec138f9dcd	backchannel.logout.session.required	true
0f933f03-43e3-4b50-8a06-ecec138f9dcd	backchannel.logout.revoke.offline.tokens	false
0f933f03-43e3-4b50-8a06-ecec138f9dcd	display.on.consent.screen	false
0f933f03-43e3-4b50-8a06-ecec138f9dcd	x509.allow.regex.pattern.comparison	false
0f933f03-43e3-4b50-8a06-ecec138f9dcd	post.logout.redirect.uris	http://localhost:3002/*
\.


--
-- Data for Name: client_auth_flow_bindings; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_auth_flow_bindings (client_id, flow_id, binding_name) FROM stdin;
\.


--
-- Data for Name: client_initial_access; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_initial_access (id, realm_id, "timestamp", expiration, count, remaining_count) FROM stdin;
affc405b-5d2a-41a7-bcc1-04d4f6a7b47b	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	1758771162	863913600	1	1
\.


--
-- Data for Name: client_node_registrations; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_node_registrations (client_id, value, name) FROM stdin;
\.


--
-- Data for Name: client_scope; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_scope (id, name, realm_id, description, protocol) FROM stdin;
448d81ba-6642-43a9-aaa0-baf38053cc96	offline_access	14fac002-cb23-4725-a783-9c18edf80bf3	OpenID Connect built-in scope: offline_access	openid-connect
d96ac1b2-3d93-4f42-9638-18f1948cb297	role_list	14fac002-cb23-4725-a783-9c18edf80bf3	SAML role list	saml
e4c684ee-1633-4731-8261-e226a26abcb9	profile	14fac002-cb23-4725-a783-9c18edf80bf3	OpenID Connect built-in scope: profile	openid-connect
97d27c99-ba37-4c08-8d4e-486fee534821	email	14fac002-cb23-4725-a783-9c18edf80bf3	OpenID Connect built-in scope: email	openid-connect
df8c6c75-840a-452d-b7ce-c500c419fc03	address	14fac002-cb23-4725-a783-9c18edf80bf3	OpenID Connect built-in scope: address	openid-connect
966cabda-1a35-4cb7-a055-bd6d52e7c61d	phone	14fac002-cb23-4725-a783-9c18edf80bf3	OpenID Connect built-in scope: phone	openid-connect
58948aa3-da26-4aca-88a4-c97f46c33083	roles	14fac002-cb23-4725-a783-9c18edf80bf3	OpenID Connect scope for add user roles to the access token	openid-connect
6c51377d-5034-413d-8851-d472093f9bdb	web-origins	14fac002-cb23-4725-a783-9c18edf80bf3	OpenID Connect scope for add allowed web origins to the access token	openid-connect
0ada5da4-3be3-4c4f-9839-82f104818b4f	microprofile-jwt	14fac002-cb23-4725-a783-9c18edf80bf3	Microprofile - JWT built-in scope	openid-connect
bcd933cc-bcb8-4a07-b038-b8e374c4a46f	acr	14fac002-cb23-4725-a783-9c18edf80bf3	OpenID Connect scope for add acr (authentication context class reference) to the token	openid-connect
79846102-8d69-4b20-b755-d82b12e29141	offline_access	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OpenID Connect built-in scope: offline_access	openid-connect
5c732fc0-2970-426a-ab23-2c6c18e3a40f	role_list	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	SAML role list	saml
acea0abc-bad0-463c-852a-e8a010d5b295	profile	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OpenID Connect built-in scope: profile	openid-connect
2c3ba811-421d-47a8-9015-ba3ee72d6234	email	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OpenID Connect built-in scope: email	openid-connect
60ff02d4-9d59-42ea-94ac-133798a98410	address	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OpenID Connect built-in scope: address	openid-connect
cee67bd3-4b78-4e93-ac4a-1c8da33208a2	phone	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OpenID Connect built-in scope: phone	openid-connect
19640b5f-a21f-4c16-9c0c-30c546452054	roles	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OpenID Connect scope for add user roles to the access token	openid-connect
5d09c6a8-9864-49bd-8ee3-3161e56d1139	web-origins	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OpenID Connect scope for add allowed web origins to the access token	openid-connect
9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	microprofile-jwt	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	Microprofile - JWT built-in scope	openid-connect
4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	acr	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OpenID Connect scope for add acr (authentication context class reference) to the token	openid-connect
\.


--
-- Data for Name: client_scope_attributes; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_scope_attributes (scope_id, value, name) FROM stdin;
448d81ba-6642-43a9-aaa0-baf38053cc96	true	display.on.consent.screen
448d81ba-6642-43a9-aaa0-baf38053cc96	${offlineAccessScopeConsentText}	consent.screen.text
d96ac1b2-3d93-4f42-9638-18f1948cb297	true	display.on.consent.screen
d96ac1b2-3d93-4f42-9638-18f1948cb297	${samlRoleListScopeConsentText}	consent.screen.text
e4c684ee-1633-4731-8261-e226a26abcb9	true	display.on.consent.screen
e4c684ee-1633-4731-8261-e226a26abcb9	${profileScopeConsentText}	consent.screen.text
e4c684ee-1633-4731-8261-e226a26abcb9	true	include.in.token.scope
97d27c99-ba37-4c08-8d4e-486fee534821	true	display.on.consent.screen
97d27c99-ba37-4c08-8d4e-486fee534821	${emailScopeConsentText}	consent.screen.text
97d27c99-ba37-4c08-8d4e-486fee534821	true	include.in.token.scope
df8c6c75-840a-452d-b7ce-c500c419fc03	true	display.on.consent.screen
df8c6c75-840a-452d-b7ce-c500c419fc03	${addressScopeConsentText}	consent.screen.text
df8c6c75-840a-452d-b7ce-c500c419fc03	true	include.in.token.scope
966cabda-1a35-4cb7-a055-bd6d52e7c61d	true	display.on.consent.screen
966cabda-1a35-4cb7-a055-bd6d52e7c61d	${phoneScopeConsentText}	consent.screen.text
966cabda-1a35-4cb7-a055-bd6d52e7c61d	true	include.in.token.scope
58948aa3-da26-4aca-88a4-c97f46c33083	true	display.on.consent.screen
58948aa3-da26-4aca-88a4-c97f46c33083	${rolesScopeConsentText}	consent.screen.text
58948aa3-da26-4aca-88a4-c97f46c33083	false	include.in.token.scope
6c51377d-5034-413d-8851-d472093f9bdb	false	display.on.consent.screen
6c51377d-5034-413d-8851-d472093f9bdb		consent.screen.text
6c51377d-5034-413d-8851-d472093f9bdb	false	include.in.token.scope
0ada5da4-3be3-4c4f-9839-82f104818b4f	false	display.on.consent.screen
0ada5da4-3be3-4c4f-9839-82f104818b4f	true	include.in.token.scope
bcd933cc-bcb8-4a07-b038-b8e374c4a46f	false	display.on.consent.screen
bcd933cc-bcb8-4a07-b038-b8e374c4a46f	false	include.in.token.scope
79846102-8d69-4b20-b755-d82b12e29141	true	display.on.consent.screen
79846102-8d69-4b20-b755-d82b12e29141	${offlineAccessScopeConsentText}	consent.screen.text
5c732fc0-2970-426a-ab23-2c6c18e3a40f	true	display.on.consent.screen
5c732fc0-2970-426a-ab23-2c6c18e3a40f	${samlRoleListScopeConsentText}	consent.screen.text
acea0abc-bad0-463c-852a-e8a010d5b295	true	display.on.consent.screen
acea0abc-bad0-463c-852a-e8a010d5b295	${profileScopeConsentText}	consent.screen.text
acea0abc-bad0-463c-852a-e8a010d5b295	true	include.in.token.scope
2c3ba811-421d-47a8-9015-ba3ee72d6234	true	display.on.consent.screen
2c3ba811-421d-47a8-9015-ba3ee72d6234	${emailScopeConsentText}	consent.screen.text
2c3ba811-421d-47a8-9015-ba3ee72d6234	true	include.in.token.scope
60ff02d4-9d59-42ea-94ac-133798a98410	true	display.on.consent.screen
60ff02d4-9d59-42ea-94ac-133798a98410	${addressScopeConsentText}	consent.screen.text
60ff02d4-9d59-42ea-94ac-133798a98410	true	include.in.token.scope
cee67bd3-4b78-4e93-ac4a-1c8da33208a2	true	display.on.consent.screen
cee67bd3-4b78-4e93-ac4a-1c8da33208a2	${phoneScopeConsentText}	consent.screen.text
cee67bd3-4b78-4e93-ac4a-1c8da33208a2	true	include.in.token.scope
19640b5f-a21f-4c16-9c0c-30c546452054	true	display.on.consent.screen
19640b5f-a21f-4c16-9c0c-30c546452054	${rolesScopeConsentText}	consent.screen.text
19640b5f-a21f-4c16-9c0c-30c546452054	false	include.in.token.scope
5d09c6a8-9864-49bd-8ee3-3161e56d1139	false	display.on.consent.screen
5d09c6a8-9864-49bd-8ee3-3161e56d1139		consent.screen.text
5d09c6a8-9864-49bd-8ee3-3161e56d1139	false	include.in.token.scope
9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	false	display.on.consent.screen
9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	true	include.in.token.scope
4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	false	display.on.consent.screen
4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	false	include.in.token.scope
\.


--
-- Data for Name: client_scope_client; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_scope_client (client_id, scope_id, default_scope) FROM stdin;
6959959b-71a5-48a6-862a-cee83b060340	e4c684ee-1633-4731-8261-e226a26abcb9	t
6959959b-71a5-48a6-862a-cee83b060340	58948aa3-da26-4aca-88a4-c97f46c33083	t
6959959b-71a5-48a6-862a-cee83b060340	6c51377d-5034-413d-8851-d472093f9bdb	t
6959959b-71a5-48a6-862a-cee83b060340	97d27c99-ba37-4c08-8d4e-486fee534821	t
6959959b-71a5-48a6-862a-cee83b060340	bcd933cc-bcb8-4a07-b038-b8e374c4a46f	t
6959959b-71a5-48a6-862a-cee83b060340	448d81ba-6642-43a9-aaa0-baf38053cc96	f
6959959b-71a5-48a6-862a-cee83b060340	df8c6c75-840a-452d-b7ce-c500c419fc03	f
6959959b-71a5-48a6-862a-cee83b060340	0ada5da4-3be3-4c4f-9839-82f104818b4f	f
6959959b-71a5-48a6-862a-cee83b060340	966cabda-1a35-4cb7-a055-bd6d52e7c61d	f
f9934ba3-18fc-459e-a566-40290c557df0	e4c684ee-1633-4731-8261-e226a26abcb9	t
f9934ba3-18fc-459e-a566-40290c557df0	58948aa3-da26-4aca-88a4-c97f46c33083	t
f9934ba3-18fc-459e-a566-40290c557df0	6c51377d-5034-413d-8851-d472093f9bdb	t
f9934ba3-18fc-459e-a566-40290c557df0	97d27c99-ba37-4c08-8d4e-486fee534821	t
f9934ba3-18fc-459e-a566-40290c557df0	bcd933cc-bcb8-4a07-b038-b8e374c4a46f	t
f9934ba3-18fc-459e-a566-40290c557df0	448d81ba-6642-43a9-aaa0-baf38053cc96	f
f9934ba3-18fc-459e-a566-40290c557df0	df8c6c75-840a-452d-b7ce-c500c419fc03	f
f9934ba3-18fc-459e-a566-40290c557df0	0ada5da4-3be3-4c4f-9839-82f104818b4f	f
f9934ba3-18fc-459e-a566-40290c557df0	966cabda-1a35-4cb7-a055-bd6d52e7c61d	f
47e58864-b732-4b3c-853b-8dcf9fec7838	e4c684ee-1633-4731-8261-e226a26abcb9	t
47e58864-b732-4b3c-853b-8dcf9fec7838	58948aa3-da26-4aca-88a4-c97f46c33083	t
47e58864-b732-4b3c-853b-8dcf9fec7838	6c51377d-5034-413d-8851-d472093f9bdb	t
47e58864-b732-4b3c-853b-8dcf9fec7838	97d27c99-ba37-4c08-8d4e-486fee534821	t
47e58864-b732-4b3c-853b-8dcf9fec7838	bcd933cc-bcb8-4a07-b038-b8e374c4a46f	t
47e58864-b732-4b3c-853b-8dcf9fec7838	448d81ba-6642-43a9-aaa0-baf38053cc96	f
47e58864-b732-4b3c-853b-8dcf9fec7838	df8c6c75-840a-452d-b7ce-c500c419fc03	f
47e58864-b732-4b3c-853b-8dcf9fec7838	0ada5da4-3be3-4c4f-9839-82f104818b4f	f
47e58864-b732-4b3c-853b-8dcf9fec7838	966cabda-1a35-4cb7-a055-bd6d52e7c61d	f
5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	e4c684ee-1633-4731-8261-e226a26abcb9	t
5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	58948aa3-da26-4aca-88a4-c97f46c33083	t
5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	6c51377d-5034-413d-8851-d472093f9bdb	t
5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	97d27c99-ba37-4c08-8d4e-486fee534821	t
5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	bcd933cc-bcb8-4a07-b038-b8e374c4a46f	t
5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	448d81ba-6642-43a9-aaa0-baf38053cc96	f
5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	df8c6c75-840a-452d-b7ce-c500c419fc03	f
5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	0ada5da4-3be3-4c4f-9839-82f104818b4f	f
5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	966cabda-1a35-4cb7-a055-bd6d52e7c61d	f
ac0e21be-bc92-4767-8f0a-4ac31622d64e	e4c684ee-1633-4731-8261-e226a26abcb9	t
ac0e21be-bc92-4767-8f0a-4ac31622d64e	58948aa3-da26-4aca-88a4-c97f46c33083	t
ac0e21be-bc92-4767-8f0a-4ac31622d64e	6c51377d-5034-413d-8851-d472093f9bdb	t
ac0e21be-bc92-4767-8f0a-4ac31622d64e	97d27c99-ba37-4c08-8d4e-486fee534821	t
ac0e21be-bc92-4767-8f0a-4ac31622d64e	bcd933cc-bcb8-4a07-b038-b8e374c4a46f	t
ac0e21be-bc92-4767-8f0a-4ac31622d64e	448d81ba-6642-43a9-aaa0-baf38053cc96	f
ac0e21be-bc92-4767-8f0a-4ac31622d64e	df8c6c75-840a-452d-b7ce-c500c419fc03	f
ac0e21be-bc92-4767-8f0a-4ac31622d64e	0ada5da4-3be3-4c4f-9839-82f104818b4f	f
ac0e21be-bc92-4767-8f0a-4ac31622d64e	966cabda-1a35-4cb7-a055-bd6d52e7c61d	f
decb93ba-5e1b-4e0d-b297-1b3f69532f47	e4c684ee-1633-4731-8261-e226a26abcb9	t
decb93ba-5e1b-4e0d-b297-1b3f69532f47	58948aa3-da26-4aca-88a4-c97f46c33083	t
decb93ba-5e1b-4e0d-b297-1b3f69532f47	6c51377d-5034-413d-8851-d472093f9bdb	t
decb93ba-5e1b-4e0d-b297-1b3f69532f47	97d27c99-ba37-4c08-8d4e-486fee534821	t
decb93ba-5e1b-4e0d-b297-1b3f69532f47	bcd933cc-bcb8-4a07-b038-b8e374c4a46f	t
decb93ba-5e1b-4e0d-b297-1b3f69532f47	448d81ba-6642-43a9-aaa0-baf38053cc96	f
decb93ba-5e1b-4e0d-b297-1b3f69532f47	df8c6c75-840a-452d-b7ce-c500c419fc03	f
decb93ba-5e1b-4e0d-b297-1b3f69532f47	0ada5da4-3be3-4c4f-9839-82f104818b4f	f
decb93ba-5e1b-4e0d-b297-1b3f69532f47	966cabda-1a35-4cb7-a055-bd6d52e7c61d	f
d935986d-48a9-4dfd-a6ef-215644092a3f	19640b5f-a21f-4c16-9c0c-30c546452054	t
d935986d-48a9-4dfd-a6ef-215644092a3f	acea0abc-bad0-463c-852a-e8a010d5b295	t
d935986d-48a9-4dfd-a6ef-215644092a3f	2c3ba811-421d-47a8-9015-ba3ee72d6234	t
d935986d-48a9-4dfd-a6ef-215644092a3f	5d09c6a8-9864-49bd-8ee3-3161e56d1139	t
d935986d-48a9-4dfd-a6ef-215644092a3f	4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	t
d935986d-48a9-4dfd-a6ef-215644092a3f	cee67bd3-4b78-4e93-ac4a-1c8da33208a2	f
d935986d-48a9-4dfd-a6ef-215644092a3f	60ff02d4-9d59-42ea-94ac-133798a98410	f
d935986d-48a9-4dfd-a6ef-215644092a3f	79846102-8d69-4b20-b755-d82b12e29141	f
d935986d-48a9-4dfd-a6ef-215644092a3f	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	f
136f4d7c-de2f-40e0-ba23-c3183b70c007	19640b5f-a21f-4c16-9c0c-30c546452054	t
136f4d7c-de2f-40e0-ba23-c3183b70c007	acea0abc-bad0-463c-852a-e8a010d5b295	t
136f4d7c-de2f-40e0-ba23-c3183b70c007	2c3ba811-421d-47a8-9015-ba3ee72d6234	t
136f4d7c-de2f-40e0-ba23-c3183b70c007	5d09c6a8-9864-49bd-8ee3-3161e56d1139	t
136f4d7c-de2f-40e0-ba23-c3183b70c007	4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	t
136f4d7c-de2f-40e0-ba23-c3183b70c007	cee67bd3-4b78-4e93-ac4a-1c8da33208a2	f
136f4d7c-de2f-40e0-ba23-c3183b70c007	60ff02d4-9d59-42ea-94ac-133798a98410	f
136f4d7c-de2f-40e0-ba23-c3183b70c007	79846102-8d69-4b20-b755-d82b12e29141	f
136f4d7c-de2f-40e0-ba23-c3183b70c007	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	f
b5ba0829-4072-406d-a674-7fe4fd847617	19640b5f-a21f-4c16-9c0c-30c546452054	t
b5ba0829-4072-406d-a674-7fe4fd847617	acea0abc-bad0-463c-852a-e8a010d5b295	t
b5ba0829-4072-406d-a674-7fe4fd847617	2c3ba811-421d-47a8-9015-ba3ee72d6234	t
b5ba0829-4072-406d-a674-7fe4fd847617	5d09c6a8-9864-49bd-8ee3-3161e56d1139	t
b5ba0829-4072-406d-a674-7fe4fd847617	4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	t
b5ba0829-4072-406d-a674-7fe4fd847617	cee67bd3-4b78-4e93-ac4a-1c8da33208a2	f
b5ba0829-4072-406d-a674-7fe4fd847617	60ff02d4-9d59-42ea-94ac-133798a98410	f
b5ba0829-4072-406d-a674-7fe4fd847617	79846102-8d69-4b20-b755-d82b12e29141	f
b5ba0829-4072-406d-a674-7fe4fd847617	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	f
05465146-c8ef-43c7-b824-29857a1e181e	19640b5f-a21f-4c16-9c0c-30c546452054	t
05465146-c8ef-43c7-b824-29857a1e181e	acea0abc-bad0-463c-852a-e8a010d5b295	t
05465146-c8ef-43c7-b824-29857a1e181e	2c3ba811-421d-47a8-9015-ba3ee72d6234	t
05465146-c8ef-43c7-b824-29857a1e181e	5d09c6a8-9864-49bd-8ee3-3161e56d1139	t
05465146-c8ef-43c7-b824-29857a1e181e	4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	t
05465146-c8ef-43c7-b824-29857a1e181e	cee67bd3-4b78-4e93-ac4a-1c8da33208a2	f
05465146-c8ef-43c7-b824-29857a1e181e	60ff02d4-9d59-42ea-94ac-133798a98410	f
05465146-c8ef-43c7-b824-29857a1e181e	79846102-8d69-4b20-b755-d82b12e29141	f
05465146-c8ef-43c7-b824-29857a1e181e	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	f
740e44d0-d37d-4bf6-a17e-31556b54fa34	19640b5f-a21f-4c16-9c0c-30c546452054	t
740e44d0-d37d-4bf6-a17e-31556b54fa34	acea0abc-bad0-463c-852a-e8a010d5b295	t
740e44d0-d37d-4bf6-a17e-31556b54fa34	2c3ba811-421d-47a8-9015-ba3ee72d6234	t
740e44d0-d37d-4bf6-a17e-31556b54fa34	5d09c6a8-9864-49bd-8ee3-3161e56d1139	t
740e44d0-d37d-4bf6-a17e-31556b54fa34	4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	t
740e44d0-d37d-4bf6-a17e-31556b54fa34	cee67bd3-4b78-4e93-ac4a-1c8da33208a2	f
740e44d0-d37d-4bf6-a17e-31556b54fa34	60ff02d4-9d59-42ea-94ac-133798a98410	f
740e44d0-d37d-4bf6-a17e-31556b54fa34	79846102-8d69-4b20-b755-d82b12e29141	f
740e44d0-d37d-4bf6-a17e-31556b54fa34	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	f
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	19640b5f-a21f-4c16-9c0c-30c546452054	t
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	acea0abc-bad0-463c-852a-e8a010d5b295	t
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	2c3ba811-421d-47a8-9015-ba3ee72d6234	t
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	5d09c6a8-9864-49bd-8ee3-3161e56d1139	t
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	t
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	cee67bd3-4b78-4e93-ac4a-1c8da33208a2	f
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	60ff02d4-9d59-42ea-94ac-133798a98410	f
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	79846102-8d69-4b20-b755-d82b12e29141	f
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	f
572b0c4d-ce45-4197-9815-f797e44d25d5	19640b5f-a21f-4c16-9c0c-30c546452054	t
572b0c4d-ce45-4197-9815-f797e44d25d5	acea0abc-bad0-463c-852a-e8a010d5b295	t
572b0c4d-ce45-4197-9815-f797e44d25d5	2c3ba811-421d-47a8-9015-ba3ee72d6234	t
572b0c4d-ce45-4197-9815-f797e44d25d5	5d09c6a8-9864-49bd-8ee3-3161e56d1139	t
572b0c4d-ce45-4197-9815-f797e44d25d5	4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	t
572b0c4d-ce45-4197-9815-f797e44d25d5	cee67bd3-4b78-4e93-ac4a-1c8da33208a2	f
572b0c4d-ce45-4197-9815-f797e44d25d5	60ff02d4-9d59-42ea-94ac-133798a98410	f
572b0c4d-ce45-4197-9815-f797e44d25d5	79846102-8d69-4b20-b755-d82b12e29141	f
572b0c4d-ce45-4197-9815-f797e44d25d5	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	f
3ad38726-150e-49ce-8e3d-a627624b9af5	19640b5f-a21f-4c16-9c0c-30c546452054	t
3ad38726-150e-49ce-8e3d-a627624b9af5	acea0abc-bad0-463c-852a-e8a010d5b295	t
3ad38726-150e-49ce-8e3d-a627624b9af5	2c3ba811-421d-47a8-9015-ba3ee72d6234	t
3ad38726-150e-49ce-8e3d-a627624b9af5	5d09c6a8-9864-49bd-8ee3-3161e56d1139	t
3ad38726-150e-49ce-8e3d-a627624b9af5	4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	t
3ad38726-150e-49ce-8e3d-a627624b9af5	cee67bd3-4b78-4e93-ac4a-1c8da33208a2	f
3ad38726-150e-49ce-8e3d-a627624b9af5	60ff02d4-9d59-42ea-94ac-133798a98410	f
3ad38726-150e-49ce-8e3d-a627624b9af5	79846102-8d69-4b20-b755-d82b12e29141	f
3ad38726-150e-49ce-8e3d-a627624b9af5	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	f
0f933f03-43e3-4b50-8a06-ecec138f9dcd	19640b5f-a21f-4c16-9c0c-30c546452054	t
0f933f03-43e3-4b50-8a06-ecec138f9dcd	acea0abc-bad0-463c-852a-e8a010d5b295	t
0f933f03-43e3-4b50-8a06-ecec138f9dcd	2c3ba811-421d-47a8-9015-ba3ee72d6234	t
0f933f03-43e3-4b50-8a06-ecec138f9dcd	5d09c6a8-9864-49bd-8ee3-3161e56d1139	t
0f933f03-43e3-4b50-8a06-ecec138f9dcd	4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	t
0f933f03-43e3-4b50-8a06-ecec138f9dcd	cee67bd3-4b78-4e93-ac4a-1c8da33208a2	f
0f933f03-43e3-4b50-8a06-ecec138f9dcd	60ff02d4-9d59-42ea-94ac-133798a98410	f
0f933f03-43e3-4b50-8a06-ecec138f9dcd	79846102-8d69-4b20-b755-d82b12e29141	f
0f933f03-43e3-4b50-8a06-ecec138f9dcd	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	f
\.


--
-- Data for Name: client_scope_role_mapping; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_scope_role_mapping (scope_id, role_id) FROM stdin;
448d81ba-6642-43a9-aaa0-baf38053cc96	48f095eb-56dc-4d2f-ba8c-1376fb23a7ae
79846102-8d69-4b20-b755-d82b12e29141	cdeaf4d6-ddf9-4be6-9f4b-d6483659896b
\.


--
-- Data for Name: client_session; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_session (id, client_id, redirect_uri, state, "timestamp", session_id, auth_method, realm_id, auth_user_id, current_action) FROM stdin;
\.


--
-- Data for Name: client_session_auth_status; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_session_auth_status (authenticator, status, client_session) FROM stdin;
\.


--
-- Data for Name: client_session_note; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_session_note (name, value, client_session) FROM stdin;
\.


--
-- Data for Name: client_session_prot_mapper; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_session_prot_mapper (protocol_mapper_id, client_session) FROM stdin;
\.


--
-- Data for Name: client_session_role; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_session_role (role_id, client_session) FROM stdin;
\.


--
-- Data for Name: client_user_session_note; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.client_user_session_note (name, value, client_session) FROM stdin;
\.


--
-- Data for Name: component; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.component (id, name, parent_id, provider_id, provider_type, realm_id, sub_type) FROM stdin;
e60a4e7a-166d-4f6f-a3cb-f47e846e958a	Trusted Hosts	14fac002-cb23-4725-a783-9c18edf80bf3	trusted-hosts	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	14fac002-cb23-4725-a783-9c18edf80bf3	anonymous
7368d9a0-4ed5-4e86-b228-ff820a4a1dc0	Consent Required	14fac002-cb23-4725-a783-9c18edf80bf3	consent-required	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	14fac002-cb23-4725-a783-9c18edf80bf3	anonymous
5f4d2eb7-e579-467a-b87f-74c6053971d1	Full Scope Disabled	14fac002-cb23-4725-a783-9c18edf80bf3	scope	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	14fac002-cb23-4725-a783-9c18edf80bf3	anonymous
ef447af6-3d5d-411a-baa7-7151b6de7e70	Max Clients Limit	14fac002-cb23-4725-a783-9c18edf80bf3	max-clients	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	14fac002-cb23-4725-a783-9c18edf80bf3	anonymous
60f1547c-450a-45d4-b8e2-1d4c63993c69	Allowed Protocol Mapper Types	14fac002-cb23-4725-a783-9c18edf80bf3	allowed-protocol-mappers	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	14fac002-cb23-4725-a783-9c18edf80bf3	anonymous
6cb5caa6-4d82-440f-8df0-5e1da49e6f1d	Allowed Client Scopes	14fac002-cb23-4725-a783-9c18edf80bf3	allowed-client-templates	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	14fac002-cb23-4725-a783-9c18edf80bf3	anonymous
c687a6b7-2df0-4249-aebe-39545fc6eaa9	Allowed Protocol Mapper Types	14fac002-cb23-4725-a783-9c18edf80bf3	allowed-protocol-mappers	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	14fac002-cb23-4725-a783-9c18edf80bf3	authenticated
c0f0a51c-5a30-4784-8e37-a51b84589084	Allowed Client Scopes	14fac002-cb23-4725-a783-9c18edf80bf3	allowed-client-templates	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	14fac002-cb23-4725-a783-9c18edf80bf3	authenticated
74dc6ae2-cbcf-4bd6-a80f-3984cb808d4c	rsa-generated	14fac002-cb23-4725-a783-9c18edf80bf3	rsa-generated	org.keycloak.keys.KeyProvider	14fac002-cb23-4725-a783-9c18edf80bf3	\N
3a3060fd-4a95-43f0-9353-e66ec41bcb0f	rsa-enc-generated	14fac002-cb23-4725-a783-9c18edf80bf3	rsa-enc-generated	org.keycloak.keys.KeyProvider	14fac002-cb23-4725-a783-9c18edf80bf3	\N
f432c7f8-d68d-40c0-8ca6-537c6a2b4d51	hmac-generated	14fac002-cb23-4725-a783-9c18edf80bf3	hmac-generated	org.keycloak.keys.KeyProvider	14fac002-cb23-4725-a783-9c18edf80bf3	\N
40761e29-3634-4f75-b510-988962ddf7b5	aes-generated	14fac002-cb23-4725-a783-9c18edf80bf3	aes-generated	org.keycloak.keys.KeyProvider	14fac002-cb23-4725-a783-9c18edf80bf3	\N
bbaa9924-501a-4b8a-8981-5fde0bde9ef7	rsa-generated	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	rsa-generated	org.keycloak.keys.KeyProvider	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N
5f6b95be-2d2a-47ff-baea-8006311a1ac4	rsa-enc-generated	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	rsa-enc-generated	org.keycloak.keys.KeyProvider	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N
f8c82960-676e-489e-b48a-e0524f237f6b	hmac-generated	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	hmac-generated	org.keycloak.keys.KeyProvider	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N
1c3f0a0d-2805-4afe-91e1-ec621c53bc4b	aes-generated	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	aes-generated	org.keycloak.keys.KeyProvider	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N
bd1c35fa-9100-4f08-a68d-1867904e6bd6	Trusted Hosts	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	trusted-hosts	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	anonymous
e70c6556-8927-45f9-9986-98cd250d8893	Consent Required	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	consent-required	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	anonymous
0fd95db5-bfa9-4346-8ac1-de3a66319ce3	Full Scope Disabled	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	scope	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	anonymous
9c38c62f-50c8-4894-b4f7-fff622c7af29	Max Clients Limit	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	max-clients	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	anonymous
9633de2e-24c3-47b8-b38c-6d76010bf5b2	Allowed Protocol Mapper Types	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	allowed-protocol-mappers	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	anonymous
2e6abfa1-a286-4d36-958f-32977c36b0be	Allowed Client Scopes	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	allowed-client-templates	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	anonymous
d25707f6-21ca-4698-a3f5-56a0650e29a1	Allowed Protocol Mapper Types	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	allowed-protocol-mappers	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	authenticated
a5194574-69ce-4d1d-abad-81127abe8f10	Allowed Client Scopes	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	allowed-client-templates	org.keycloak.services.clientregistration.policy.ClientRegistrationPolicy	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	authenticated
\.


--
-- Data for Name: component_config; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.component_config (id, component_id, name, value) FROM stdin;
dffc8aac-8171-4ba0-b776-59f6d00b4b6c	ef447af6-3d5d-411a-baa7-7151b6de7e70	max-clients	200
38406816-0bb3-4b9b-b8a7-4605691d1d93	60f1547c-450a-45d4-b8e2-1d4c63993c69	allowed-protocol-mapper-types	saml-user-property-mapper
1d190e3b-373f-4875-9c05-e5ee290da856	60f1547c-450a-45d4-b8e2-1d4c63993c69	allowed-protocol-mapper-types	oidc-usermodel-property-mapper
7c2e392f-5f2d-4f0d-9a2c-5132b811953c	60f1547c-450a-45d4-b8e2-1d4c63993c69	allowed-protocol-mapper-types	saml-user-attribute-mapper
6bb60b6c-8b48-46c1-913c-f4955db70ae8	60f1547c-450a-45d4-b8e2-1d4c63993c69	allowed-protocol-mapper-types	oidc-sha256-pairwise-sub-mapper
389a6e6d-6af4-47ae-99e3-106c9ebb49fa	60f1547c-450a-45d4-b8e2-1d4c63993c69	allowed-protocol-mapper-types	saml-role-list-mapper
1e45b3ca-1e31-4161-8362-2f82c0d8309d	60f1547c-450a-45d4-b8e2-1d4c63993c69	allowed-protocol-mapper-types	oidc-address-mapper
b43ede6f-b52a-4e4e-86bc-947466b29054	60f1547c-450a-45d4-b8e2-1d4c63993c69	allowed-protocol-mapper-types	oidc-full-name-mapper
c5fddd2d-0ab2-4aa5-8942-2fe53f6f1fb0	60f1547c-450a-45d4-b8e2-1d4c63993c69	allowed-protocol-mapper-types	oidc-usermodel-attribute-mapper
ad6afa2d-4c45-4ed3-a99c-27fce25229ff	e60a4e7a-166d-4f6f-a3cb-f47e846e958a	client-uris-must-match	true
4401b9ec-4061-45d8-be5d-186c41553073	e60a4e7a-166d-4f6f-a3cb-f47e846e958a	host-sending-registration-request-must-match	true
c9ff4530-3eb7-4e46-a8d0-c98f8c2787c3	6cb5caa6-4d82-440f-8df0-5e1da49e6f1d	allow-default-scopes	true
37a6952c-c742-4cfe-b3ad-20422ac213ee	c687a6b7-2df0-4249-aebe-39545fc6eaa9	allowed-protocol-mapper-types	oidc-usermodel-attribute-mapper
65bd24b7-cb96-4275-8904-8e65ff5353f0	c687a6b7-2df0-4249-aebe-39545fc6eaa9	allowed-protocol-mapper-types	oidc-full-name-mapper
ffeeeb0b-d253-4cda-8d88-6105a11462dc	c687a6b7-2df0-4249-aebe-39545fc6eaa9	allowed-protocol-mapper-types	oidc-sha256-pairwise-sub-mapper
0f155f58-0a56-48c3-8355-5fc3d76b45d0	c687a6b7-2df0-4249-aebe-39545fc6eaa9	allowed-protocol-mapper-types	oidc-usermodel-property-mapper
8ddfafd2-537f-4ab0-a125-a6f91e63f511	c687a6b7-2df0-4249-aebe-39545fc6eaa9	allowed-protocol-mapper-types	saml-user-property-mapper
cbb117a1-0b56-4b99-97b6-548805207bc9	c687a6b7-2df0-4249-aebe-39545fc6eaa9	allowed-protocol-mapper-types	saml-role-list-mapper
7dd1841e-785e-4d26-8d08-d7cd56e1fce9	c687a6b7-2df0-4249-aebe-39545fc6eaa9	allowed-protocol-mapper-types	oidc-address-mapper
c95abe32-745d-4bc9-8b3f-1b5ec3618df0	c687a6b7-2df0-4249-aebe-39545fc6eaa9	allowed-protocol-mapper-types	saml-user-attribute-mapper
8f7677f6-6b48-404b-8336-4d44a4053c9f	c0f0a51c-5a30-4784-8e37-a51b84589084	allow-default-scopes	true
604ab33d-3ba3-4eb8-a147-b631991a58cf	3a3060fd-4a95-43f0-9353-e66ec41bcb0f	certificate	MIICmzCCAYMCBgGZS/jNDDANBgkqhkiG9w0BAQsFADARMQ8wDQYDVQQDDAZtYXN0ZXIwHhcNMjUwOTE1MDYwMjAwWhcNMzUwOTE1MDYwMzQwWjARMQ8wDQYDVQQDDAZtYXN0ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQC8foylrnwVGpPnHoCwt6+FCQnREUpEU9DSxawjIjon9Q0v3j7vYiYvccNIp6FBTRCgU9KEPClVsdCWoKsr3aWlGzNByCOKQq012sOAB/SQmRO2T2tmQulYxC5Sx3F9QE3OnmF9VC3+yesTvC9DxLIJeEiVOFRmf/bgSQgMOu++g5hTwRQlntMd+3AO/oLkbcFV58CaZM62wLc/tnzHyNIz0zSqzzAmmYqWfcG2YI1FRr6aMA5MInCHCdNMlIdzv8jUsrKjfckfOzd7jt1RNhXUan8j3VwPJRisr09g136rZsbdwFJxOARbKnS/EpEBSzo5dXs7VCbqjBeWzySAGRONAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAGNPk8+vaFg78fV82cV/dp5uTUOJrPgsY4YFAXYZPu+yRNSBHc4nQT+VLxqOuTEMgYXgLz2XSJ6TwPyPOzVXneEZIGTDBdplBusIqQaz1UhuhHM/M6uVwe7TrJrrfpkJ8VkoKTYdmPyYJAzR3SuYdV9d8mmuTN6XvJygRBywqa08fn3DcqFNrJO2vc26md5QWyAm7I3t0w+QXxYbamqQbvLUWUrQMW1PJKugUOsOgVki6b6T7XhoIxWil2nHRKSECdzBoOssh5Ie0hh9uoHb0GW1+iFv/WsuZcn/BeUY0fyl8j0YtB0vxuCnzLnrYjQXz264c/6zmzpVLSb1p5CNZSc=
aa0be2b1-e3ca-48e0-8356-05a948ca0600	3a3060fd-4a95-43f0-9353-e66ec41bcb0f	priority	100
3e42187b-7e1f-4d7c-9422-2bcaa34aae75	3a3060fd-4a95-43f0-9353-e66ec41bcb0f	privateKey	MIIEogIBAAKCAQEAvH6Mpa58FRqT5x6AsLevhQkJ0RFKRFPQ0sWsIyI6J/UNL94+72ImL3HDSKehQU0QoFPShDwpVbHQlqCrK92lpRszQcgjikKtNdrDgAf0kJkTtk9rZkLpWMQuUsdxfUBNzp5hfVQt/snrE7wvQ8SyCXhIlThUZn/24EkIDDrvvoOYU8EUJZ7THftwDv6C5G3BVefAmmTOtsC3P7Z8x8jSM9M0qs8wJpmKln3BtmCNRUa+mjAOTCJwhwnTTJSHc7/I1LKyo33JHzs3e47dUTYV1Gp/I91cDyUYrK9PYNd+q2bG3cBScTgEWyp0vxKRAUs6OXV7O1Qm6owXls8kgBkTjQIDAQABAoIBAAW9RrQOxYlw9zrs01dOKIKA/XMHtz6wXKXmTfD97E5mKEIiXsE0OFxudtdB/2d5HL38/2Z2XH3W/bZwavXZeF84Hk3s9aQSlqqSoCxIUbqLZq3A6lDBTdCdnJl5qCt7p0LY0LgbtZDR/h3qN3g1R4SBTJh6kBtU3yQCJUQWEVG4K5i+HwhjCcFoq9CGQT+hNirxvuvjqEc13pL4gxUN28HLQ5tZ+gka1qpfdE3n87Z2HkpKub+AnOxumrX5hVcrnFPxqtbdjljIcZmo7wajqeBTU30iJXz4bW9rzEvsb2ZlT0hIQqcA0cslVb7sS/ij7/ZU+p7V4cqp7WIfmCqKq3MCgYEA9PZZjFCDnyIUZ9V8nH7ozjQSr9Hba4b5Us1LVairZs/5uOCBMsnNk0XAiCLUuu0ocJs6ksOhDVb1UfymsEIP+p1gzi5vScIAKsJD8UbjbtrRlIyo79vJFCDwOEOl8Oal2wCitWABzbp4fa1OS61V2rXKiyE3FD2kMli7uxJGPQcCgYEAxPzW2Y0gkqr2NKbM6fB2B7PV+xXSCp7LNFv0/f8dUMAmiJqy5zjkXtE0ckw/uzMAH+M/HXfo7Qc3cgkl7SvKrqZrIQFai6LrE6NZ2ASW9sWdTVBfQWa/93rYuFgtNvBYR3M1Oh90lna6cjGDPfWdKVeLQbem1rHRHkKiCBrHGcsCgYBiHsdRv/Jo7JK14LqwSNlht1QA3xN/56m4tAjH/+7gGNBTcIHv4QuaQSdUoHcLhPFc4aC9puS83icXtxbhpSSXYzCihdEH/1Bn0Da8z4NMJRQr54fHxAawLF1sfBRdAVZP+doywokWaenXlXK2N3EbURQXVK+mSOabUBWqYjCGQwKBgAF3gIIOMvfkngC2XUBAsiBMjoNYHkBF/m1dYe2iQK2hQKgzvrFKEylBlYpkVvUl5BJYtteZVPqu2xLWTpn9gXzbcGDGic4QYH+876ZUImuj/Q/eUVdDVJqbJ5H31h5am82sZfEQ2uVj7O98vuVvNsRgCoFqxKtEl7IJ/PrieCkRAoGASDgWaMQwgKZkAZSm/u5EfkGdAayYCM4NMRPEXugk3E82NxqTZ70HgyPC018I9+kFZHDF5a9WRSQqkY2fQUDpWFQH+I5/t1pr/1JZ69N88D/oRYbgl5pJS/yfsXm+nYUoOWTYYW27gnYMl0JHRfuDSUO3M0wot6DYGplV67m8Ujk=
837b202b-b529-495f-a575-cc8e1d4cdc13	3a3060fd-4a95-43f0-9353-e66ec41bcb0f	algorithm	RSA-OAEP
242f9447-f290-49ad-b170-008753761416	3a3060fd-4a95-43f0-9353-e66ec41bcb0f	keyUse	ENC
47351df2-c2e2-4729-8113-3469c937662e	74dc6ae2-cbcf-4bd6-a80f-3984cb808d4c	keyUse	SIG
1d000703-3371-451d-9ebe-871be4dc4b3e	74dc6ae2-cbcf-4bd6-a80f-3984cb808d4c	privateKey	MIIEogIBAAKCAQEAqA6ZaL3LdtiIWuAroJHdtLbnaHyNR+OmeYXZ8we9kOTBrsPV93uyOk+zRgvpcEAo+oJVoCrdRrGAzmXKLUL5wU001dHU5P+xc0lUp8xA188uUHYkZLwrUgeObjVcb21NOOWTJveFOY3dqHWiSDSA7+7KcVwCZiTLlqVhhN+HGPF0L5EAWAMy6Qtti78VwqAvepWcb6L6mm+vYjNclib9UzCvvuSKU2msiJqHzlM+lR+oKj0uo5jiFCiUYCueWyMn2amcd9mzVTIyX7eCDKA1vDihi7zrabqSJB6lq7XW/sYDTvZtlxi2EkSh+aEYMrScbcGpyFDyRmPDkibngRVqlwIDAQABAoIBAEsir9LE3DUnzRq4E7lHzdQQXDh6Sx4DeTfmsJE3+jhQFgnhM4Xgr0fjvzaHd9hATzHk2Ixx7ZlAHeLpXnFK+ufB8WfRVvoFGVx+QhN9PdZeognGMDI7cLDNgQ/j2o5U00wnU9l9m3omY53/7cyTcLwUyjzRUp9BPyZ8jTN/TmLHQyMTY+2bSzjzZN0OxLx3bhq6Kxk9AapHhioOBR7QCJBcaxIccHArXyDF7P09uh+IeZRwjIjdyaksfIRLAEy+IkjhwO7sKgeXIZuLexv0ShnUqdN6mB55HILYbRs+CXzABxwCHJ1wtywCVvDd5qlI1xp2v0kGGAQqnrgHGllznVECgYEA2hriXIdlet7n/1q0o6nE/DR/QoWImFD/eL6D9w95AI4u2PSS/Vcr6RFviONE45jVVwYExIP06qHd3qfYKIKQaqpQVcjylhtlb8+K/ThVWBhpuLSLh3Opm6twE1avK/EXwv0lqU50CwyADGfAW838dZuAybm27EH2SzIG8mNGWvECgYEAxUGgFptQUr6UUJUFqNrBlkWeFBfXMmRocB2LJ10Jmc9tIFIydaFWmJQS8vkiSklnFSzr3XZQSGm9yK1O9b7dcMiePl9rxO1JvwXsITtXrZ2ZJ0h3E4t2pitnOskPxAwx+yOo+0LemR0twcgpnaRKRWX42WJJRD6jFO71sE6rzgcCgYBsPSNRYDBQhgcEhvNWtY34y+TMEpDzZl8igQnktl0KlYVSTi4UZAp6pBiCCqfJe7OEeIwtKn+FYcdGFG4FzKX3CYbMy+j37aitkIYdnLNeAN+WtqL4GcJ2auI39rEAUanFLvRdzX1xbkat4V9pMBS2GPIVKoFJRoMAVld0OyhTQQKBgEr8wbEWZhiuno9rt3UThFhuKsQUtBhSF2nXsVjLg6TXN/L7jHcvWE40rGd10lVPRkLEiv3rxmVg3Wb2i3fckS4Y9h9cAwof8tMNm1Ce0JwNt3U5lRr2/6n4AgxU9wteX3nLeqfaKeknMIfkYf2twRZUx59MP/ITpceFeB0IK0PtAoGAJTQ4z5BZEVr1AvH7f3LFxwPVKggJg5CB3H73eqNBPUclS3SKP2d3MJYnXP/u7l33nyJjSg7BCnCE+kmfVOel/G3SB5taJlTWuc0NRaskLsX9ZgpnYl9OO8/tFozqLSwfcZNOOODQF8ARTW3p4SYHTBVWlyYT46yuvr3TRdr6UDU=
509b4b85-7169-4486-8fd9-92740321f5b4	74dc6ae2-cbcf-4bd6-a80f-3984cb808d4c	certificate	MIICmzCCAYMCBgGZS/jMGDANBgkqhkiG9w0BAQsFADARMQ8wDQYDVQQDDAZtYXN0ZXIwHhcNMjUwOTE1MDYwMjAwWhcNMzUwOTE1MDYwMzQwWjARMQ8wDQYDVQQDDAZtYXN0ZXIwggEiMA0GCSqGSIb3DQEBAQUAA4IBDwAwggEKAoIBAQCoDplovct22Iha4Cugkd20tudofI1H46Z5hdnzB72Q5MGuw9X3e7I6T7NGC+lwQCj6glWgKt1GsYDOZcotQvnBTTTV0dTk/7FzSVSnzEDXzy5QdiRkvCtSB45uNVxvbU045ZMm94U5jd2odaJINIDv7spxXAJmJMuWpWGE34cY8XQvkQBYAzLpC22LvxXCoC96lZxvovqab69iM1yWJv1TMK++5IpTaayImofOUz6VH6gqPS6jmOIUKJRgK55bIyfZqZx32bNVMjJft4IMoDW8OKGLvOtpupIkHqWrtdb+xgNO9m2XGLYSRKH5oRgytJxtwanIUPJGY8OSJueBFWqXAgMBAAEwDQYJKoZIhvcNAQELBQADggEBAFLalypcZ4Skt2+doP1x35aX78L/HN90TGKnpbqHbVZg8jfHZgunAbhYBJCUMmGLSUfXn2Tg+CxeoUFdV6eYTa1++ayJEKLEgN5NmVHax6CXtHfGYu6uTDjqHrBU4ogCHxgmUOKfyu91CqgqnzMFfVQF6HC9eWglrQQlx7WeV6IfAGcfUiIhaWoAk7cQeA092VUBKe5u7xDP1ReFHddyfSlQImNDO2WdaL6Yq0NAgD5OMQdqgS2kSUGNnD/M6G1sfFURXuNLUDAL0PYvPhl2Xll+OEYvu/zEfa0dcuw3EjFbSMBE5Ii07o4Fa0QJwtZWo0BaBC3/bnAWfJ7vFUiF11k=
d9f50381-7f1c-4c37-8747-d901075dd284	74dc6ae2-cbcf-4bd6-a80f-3984cb808d4c	priority	100
01a23e71-795e-4d59-aed1-79d27ae5c3d6	40761e29-3634-4f75-b510-988962ddf7b5	kid	379f6969-99b5-4dab-8298-4e720626b9b1
91946e82-0fe5-4cc1-9502-231c55cb5a1d	40761e29-3634-4f75-b510-988962ddf7b5	secret	jJAGx5K3V2Yy9a_nqkFtIw
0a0ebc2c-3f37-4ccf-913f-bd87ef92f78b	40761e29-3634-4f75-b510-988962ddf7b5	priority	100
436f063d-a2c8-4688-ad05-fef73e1ff52b	f432c7f8-d68d-40c0-8ca6-537c6a2b4d51	priority	100
8429d8c3-68ae-45cc-99c1-418392d486c4	f432c7f8-d68d-40c0-8ca6-537c6a2b4d51	kid	c0fbe142-b53a-4f2c-a0ef-d9fe7ef1bd90
eed71185-eadd-443c-9635-3e45a97f08ce	f432c7f8-d68d-40c0-8ca6-537c6a2b4d51	secret	MGkVSflkxBN3izQn8qDYbkuBSkakFGGJDcknc93D7tmMAkVBLsR-NVxPAWNrN1wOwSN94Iqa6oLlv0c5G-yCUg
04a71313-45a9-4ed9-a4d8-0a00129ce34e	f432c7f8-d68d-40c0-8ca6-537c6a2b4d51	algorithm	HS256
5e00600d-98e5-4fa0-bccb-967daefa68a0	f8c82960-676e-489e-b48a-e0524f237f6b	kid	217154c3-0d9b-41ab-8b0a-b3346006e63c
cfccaff7-ebe0-453f-9a42-087987a7bb5d	f8c82960-676e-489e-b48a-e0524f237f6b	priority	100
56c11719-4f97-4136-b7bd-c724966fa1c8	f8c82960-676e-489e-b48a-e0524f237f6b	secret	gZb2F3vS3dtSOdi0QBqZZRPh9ciC1o4N33IEL8yYTQ4sexU0pRHCcFa05WSYQR-1zYcvAPxyo0AXHiEQVYj3pw
bdcbe836-a687-4dac-8930-759c8d635121	f8c82960-676e-489e-b48a-e0524f237f6b	algorithm	HS256
7c03c85f-8532-4f45-81ce-888fafa7b78c	5f6b95be-2d2a-47ff-baea-8006311a1ac4	priority	100
1f5c3d7e-f69c-4ca4-9196-e0322d4097c5	5f6b95be-2d2a-47ff-baea-8006311a1ac4	privateKey	MIIEowIBAAKCAQEAygVPmaak5LKpbi5FkG5ytf0UyeNT3LLwE9qhCQ0nh6UbJ1cQlU0c9QgQtirlC66yLQDU+KylhD7LQa/RiSpKjxGRtVKnMuKsGEwYd/ZELBDmAiUg+9V+3ib8ATw7r5C0E+L4n0RzQXpfHuOAb5Vmb4QcPENO4aFO+fggDy4HmlNs9Eq0/97OKiSjZziisE5dnZdUm0fmgdBeTVvtmYYfDxak4Uk/vouv9zt5rUD78xxmkg1jjjhXbzy+Njl6mm+8s5NV6v/O/FG4OxblPEY5pcSvMoyCpzJMt4Vk/KoTlkJGM2MLIp3RgYcwaZKrPKSaewVaIIxvNMvc58QaAPseLQIDAQABAoIBABxcZ4LiHrcOj/OsYk62+rqq127KT70lj8exwzbCloCYALpioizboQuTQMlaN0KPGWtw+PUaQ7hPmnhTxv5TJhl0tXh3rIpbwR4+HJdGxuZKzweRD9La848bePhxbM2maml6TT4KvYV9OPpcpOzFVBamvNIG/v11CGHWYCXRZ/bVrGhXsTEoaNX4zfr5aNpQVRh7lcrzb5tQO/lLYFz3dkAK/g7kzyjzVAB2LFyNKpjApgU/mkmPy7ymCglq5O0Y2DCiIilaB2LjZNeYI2whzR96s2L5dkc+LxXP1ZR7ZVjKP9smUJbpbC/1VmaiwV7kB/sLRc8k2ITGLmNQ9lV/SB8CgYEA9OoxyiiCNNR1uTtQSXQiKON9ko43w5H+qXdNVBCF4MSjcY8gFu8l7jgTxE9d2ckJqEwTRyfvhQ/PrsJYYpmkjkSjL+IQllGp5+IacWvcUv5If8wF36INJwz4P67wcDxpp4dhw0L/9SBD4lcuwZYZQ32SnbemqEiAceA9XxmSB1sCgYEA0yobUr4NbJ+4dbtYqtML2TeSDMXeFBXfqpT+UWeBSaSi+PK55r9UQamAskWfmV35H4XqGM48GP46IuAa2ReL8SoMofrZlMSteEFdFjDY8Rg1FUvRjCSBI9R1GClffcE5x3cs4A2/3BdRg2mL4ezYh1xoCqCAK8w1EghWXebEbxcCgYBIFXnsDzorhHDVwPBjfivjrvkxnmwpQpoalMOMv4Q34IhejwqZ1W9ritO+jsGHrRULlMCDy+xCVKICxK6w7pmxSeBh4n6RT53bYv/3bBCf2LnL62e7J34JhVa3n6yizBUour76cojVQ9bXEmPvlFcBNz9k7j2+AADWA3Lsv6DsSwKBgQCsXu3I4BzjM4oGiLS7VOWSHduZk5dRu/1FKLRkzOHrtHnYT4LSyhHEO9IzM8QxFFW8lrreHtNN75CTUW55EdCJPzLqsnMUDmwwAuMyrxMX0xPZ23clN9M25UHkRDmP1MAOg8jc6DxaM6q1Y93swUKLVDIVoqidl+cmXinqIursZwKBgHZ9fjOWLaNd9x52eIGWGKyxdd6u6o4k5Bctz+ayxPhzqi0w4AVGO0CUir5Z+s6vrF4zlIrgpAwg2bLq4CbB5PFsAP6KHUVM3VioB+RJ3A5lUeHkLqNR+Pra0xc8lqwX9uQTT43BTXtFBRJ/Ev++hM/vEEAoErC4LFFjMfjs5M3g
d137f95c-374d-428f-90be-b4ae9dad255f	5f6b95be-2d2a-47ff-baea-8006311a1ac4	certificate	MIICnzCCAYcCBgGZfunoSzANBgkqhkiG9w0BAQsFADATMREwDwYDVQQDDAhwcy1yZWFsbTAeFw0yNTA5MjUwMzI2MjJaFw0zNTA5MjUwMzI4MDJaMBMxETAPBgNVBAMMCHBzLXJlYWxtMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAygVPmaak5LKpbi5FkG5ytf0UyeNT3LLwE9qhCQ0nh6UbJ1cQlU0c9QgQtirlC66yLQDU+KylhD7LQa/RiSpKjxGRtVKnMuKsGEwYd/ZELBDmAiUg+9V+3ib8ATw7r5C0E+L4n0RzQXpfHuOAb5Vmb4QcPENO4aFO+fggDy4HmlNs9Eq0/97OKiSjZziisE5dnZdUm0fmgdBeTVvtmYYfDxak4Uk/vouv9zt5rUD78xxmkg1jjjhXbzy+Njl6mm+8s5NV6v/O/FG4OxblPEY5pcSvMoyCpzJMt4Vk/KoTlkJGM2MLIp3RgYcwaZKrPKSaewVaIIxvNMvc58QaAPseLQIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQBXkA0KugZ9iXO60P681lgnKDHBV+9OLKvcLIy7Z4JnQQxHX735sZfCmu0w/qvcFcyJzLmLXQvyy1o1Cg+eFyt7D8VJHvGOFnfVzj0h1ltj8y/2DN3hhWajzZJQpq6Bx28KsImCv3WS4VJhRH8RoglBPKgGXIRbiAjasMQhkTpgTD0FPOv4av7LbhcOnlcahNz/xQPIfMLJvmOu1t/vmAbAZWSUEbmrdsVY0ckFhlcu/COOGSpz0xQD9VRO9/0ypib2oSEhG6vwGUAzqxKdObMzEIqs7PIrXE4jGaQZ7DK+9o2UJVW4P0HLa3VDL34klwgRxv0lvm20K8Z0gGoWVCOF
86a2af2d-435f-49b8-b9fe-8ad117225d4d	5f6b95be-2d2a-47ff-baea-8006311a1ac4	keyUse	ENC
ecd02dea-ebfa-4f88-93e9-ab9ebc96da6e	5f6b95be-2d2a-47ff-baea-8006311a1ac4	algorithm	RSA-OAEP
515263ea-7d85-4aae-8149-4868a10623f2	bbaa9924-501a-4b8a-8981-5fde0bde9ef7	keyUse	SIG
b1687030-d5c5-48bc-b0b8-5e4d9d8bfc63	bbaa9924-501a-4b8a-8981-5fde0bde9ef7	certificate	MIICnzCCAYcCBgGZfunnfjANBgkqhkiG9w0BAQsFADATMREwDwYDVQQDDAhwcy1yZWFsbTAeFw0yNTA5MjUwMzI2MjJaFw0zNTA5MjUwMzI4MDJaMBMxETAPBgNVBAMMCHBzLXJlYWxtMIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAloCBvKDu8JtpatexjtfQKNreXIJYfTUBE6m6v7xNj6tlx0t/jRcg8hvFccq3xxl/FPaQPcZB9rgtjHuqT70kxxfpZZDYXbV7KfeKAlC/l+kb6TYotn0RXMZ3A3yoj4B9HDAbLzTdYr43X306e/20nx0VZAj+rN0Buw58llFbbYQh0mTzVohyzd/uuVOA2njsEi/OV+6TFFiR32BOrn4OJ0PiTydb3MBrZvgzEoJVmj2Su22EpXZyaamY7k2v00wT5Lbrvb1WY6T8RQl+p5cypQyM9EvgdlxtaEKxXMpfrJyjbGXRuJ1NHuTWhIPwd9+g9hu2qy556K8P4D1DpcKWhQIDAQABMA0GCSqGSIb3DQEBCwUAA4IBAQAc4AaLju8q2b6ZU/QPVq99BKvwXxsAwiyrka9fV6N/vvEm+bCWWSpnjds6/maQMacYPqgKDhmJE1Rf/IFmFqEiDVNvpyGNtQwmONI9aer7cJDMCcC8l96Ul/+9MMSE0aPyXvGGR+6+e6BHslhMEmX42ZzyNb8lhvjDlLb8qnr6DobDXMwtKqN33vw2ZFIkJo+argle/vASJfOmW7YZHBT3IbI8b3ueTivzFlDSwMMwj7/NF9SHHDCz50HhTMA+LEXYVl9Vdi75gPZmU7tXyB9fQCJCEoI+L4Q4lu+Iu9VSpJI0qigkTYWMDN0oFb/+aYMB0dgD/Vp8afroFb91BoaV
b96ff71f-c283-42c2-9f05-3af62ed03a66	bbaa9924-501a-4b8a-8981-5fde0bde9ef7	privateKey	MIIEowIBAAKCAQEAloCBvKDu8JtpatexjtfQKNreXIJYfTUBE6m6v7xNj6tlx0t/jRcg8hvFccq3xxl/FPaQPcZB9rgtjHuqT70kxxfpZZDYXbV7KfeKAlC/l+kb6TYotn0RXMZ3A3yoj4B9HDAbLzTdYr43X306e/20nx0VZAj+rN0Buw58llFbbYQh0mTzVohyzd/uuVOA2njsEi/OV+6TFFiR32BOrn4OJ0PiTydb3MBrZvgzEoJVmj2Su22EpXZyaamY7k2v00wT5Lbrvb1WY6T8RQl+p5cypQyM9EvgdlxtaEKxXMpfrJyjbGXRuJ1NHuTWhIPwd9+g9hu2qy556K8P4D1DpcKWhQIDAQABAoIBAB9OjI5RONsGrRtbUnVpGcVB4CMEv0UOX1K4ih8j28uAxyt5nkcaclFCqyQUW/3X5SpWbuNWyDZ7EgiI2gD70rhyliu5QXTe0UfF1SjNpMlEJbjxue7aHeA7bZrlVcgCHaRdMm5ztAC8Glcr/Nw5hpbZrS8xus55K8i+Rvba5ial1agRKs+t4YXk2X/k0+dchsuq82w6Ln9yRSDEYWSU+JsBe2HLkFdZI3AKiNyH4nGNK0IERYFPTYn5WS4qzeXk7NABJNQw8nL3Pzi8grJPbGnnHbiuc1ifmM+imj55YcAxi0+02ydiA3wyWwr/PSobGxQ4T5R6SW7pZf4qzyl3fLkCgYEA0rOkaCgRXPYI003t7rwAVkwjJLCY805c3kUqhOe88nYN/ZH9AKD4gvq7HRaZEdQ7qFBGdRqWGyeRk8jYzxN4ydALlwSWKJndITsPjX/7czzx1qhjzRmHjInQX3cXJ3yZVC/6kd6+DuHvk6m5VURAnHnfDH+aHnUTqZgcLJHND7kCgYEAttupGLdtBWNexK/0dW+xd19iQdprPQjcoHPXVvUFnghsu6Rf2Tk6OF47A0BeGpbR9tPZuf3ecUBlgYKUF0hRnde8BfcmLlRoaoFs7XQ9i/njogsP0vhszQQSDh/55/0ZccFWi1Sn03vyvFRhYQPo6B1S9olEWppTjQLC3lWI6y0CgYEAqp+cwFfKD7yRUwPlbJFDuJ55SwnTIhxQKhJapzu+D544ICIrfmncNLzz709Qb1atZNR06fm4mo7FXaako7QB+XOHHykwq/pFTtaz9oMwTZaJxKmiAAHveYhUfCpFTs2goMMlpyB4Sjb+OlaT5Y+avtwWGtnuJUHrxUePcSw0ockCgYB462U0zQcxCvc+RcT4a7YFZHQLbC6CG/wZtsDt5fWi/Zs5socXlHplkBqiGcyg9J9AA/P0xGZ2qcj2Da10gARIxsK1hywecv7hf1jS5Y1VpRfcrkf2kQURxeFSqd6OhJZw3RMn7AFXHp3z4DDNANLhu5KDKV6kM22Q0AShFhsGuQKBgFN6X2dT5H+S/c6g0TiMA3xs8zEGDTaxnMTBBjVhEWpvjXwJTaMlVt87VeaNKgVOgVmK3flk/w+9u7GXP5dc8ayHhLSnbzb5KPwOZ5AC584xph/UueFWJBTTiqMTlTNGDl5lgoSDiA1lhy8NtvxR2NNXyd1WyeinZFjyNC8Y1gxk
96d97513-5cd3-4222-952b-2836bde31457	bbaa9924-501a-4b8a-8981-5fde0bde9ef7	priority	100
7fb19658-577f-43da-b48f-58dea8a1e237	1c3f0a0d-2805-4afe-91e1-ec621c53bc4b	priority	100
86374f2e-6797-4b30-a285-f4d7949809d0	1c3f0a0d-2805-4afe-91e1-ec621c53bc4b	secret	NcpgRpoBQ5bGGG2_5OX7TQ
fff7bf42-9990-4846-9368-c2ab8811811f	1c3f0a0d-2805-4afe-91e1-ec621c53bc4b	kid	67e77c5b-13e0-4b73-990a-786c695afe88
871f1f73-89d7-489a-87c8-c8652491a9f5	9633de2e-24c3-47b8-b38c-6d76010bf5b2	allowed-protocol-mapper-types	oidc-sha256-pairwise-sub-mapper
24778af4-f5fa-4709-9e04-0634fb1a8141	9633de2e-24c3-47b8-b38c-6d76010bf5b2	allowed-protocol-mapper-types	oidc-address-mapper
33863279-6ce1-4ac1-86cb-5627a940d066	9633de2e-24c3-47b8-b38c-6d76010bf5b2	allowed-protocol-mapper-types	saml-user-property-mapper
f1ba1fa2-5d26-46a6-8b92-dbcb3724755a	9633de2e-24c3-47b8-b38c-6d76010bf5b2	allowed-protocol-mapper-types	oidc-usermodel-property-mapper
a31adc6b-fd35-47b8-aff5-367d797a3fd7	9633de2e-24c3-47b8-b38c-6d76010bf5b2	allowed-protocol-mapper-types	saml-user-attribute-mapper
0748e1be-6535-4922-a689-d495c6096969	9633de2e-24c3-47b8-b38c-6d76010bf5b2	allowed-protocol-mapper-types	oidc-full-name-mapper
5e8f486f-d06b-42a6-8648-5517076c451f	9633de2e-24c3-47b8-b38c-6d76010bf5b2	allowed-protocol-mapper-types	saml-role-list-mapper
17d096bd-6072-4a15-bac6-62d6044fcf43	9633de2e-24c3-47b8-b38c-6d76010bf5b2	allowed-protocol-mapper-types	oidc-usermodel-attribute-mapper
82ba04fc-4f40-4e2b-8771-8dc453d606f0	d25707f6-21ca-4698-a3f5-56a0650e29a1	allowed-protocol-mapper-types	oidc-sha256-pairwise-sub-mapper
f8191372-cc2d-4dc3-810d-fa71b40d18c6	d25707f6-21ca-4698-a3f5-56a0650e29a1	allowed-protocol-mapper-types	oidc-usermodel-property-mapper
dab15791-383c-4669-9a51-75cede6c2a62	d25707f6-21ca-4698-a3f5-56a0650e29a1	allowed-protocol-mapper-types	oidc-address-mapper
f76418c2-8b22-4ae7-b08c-44e73ca43ad8	d25707f6-21ca-4698-a3f5-56a0650e29a1	allowed-protocol-mapper-types	saml-role-list-mapper
c5c041c6-e546-43c0-9eb1-16ea5cd2f19b	d25707f6-21ca-4698-a3f5-56a0650e29a1	allowed-protocol-mapper-types	oidc-usermodel-attribute-mapper
b3e5faa5-a0c0-4177-b234-e1cf43ea9fe4	d25707f6-21ca-4698-a3f5-56a0650e29a1	allowed-protocol-mapper-types	saml-user-attribute-mapper
8d3bf45f-ce6e-4b2c-a7e0-241f8ab5467b	d25707f6-21ca-4698-a3f5-56a0650e29a1	allowed-protocol-mapper-types	saml-user-property-mapper
3a7dc53b-e5d9-4e92-a396-694827e46eaa	d25707f6-21ca-4698-a3f5-56a0650e29a1	allowed-protocol-mapper-types	oidc-full-name-mapper
9c34ce8c-a188-443d-a653-611986b174bd	a5194574-69ce-4d1d-abad-81127abe8f10	allow-default-scopes	true
b03f7208-5d9c-483d-9df0-48017b2661a8	2e6abfa1-a286-4d36-958f-32977c36b0be	allow-default-scopes	true
753d6630-b073-4955-869c-6a10931f0f21	9c38c62f-50c8-4894-b4f7-fff622c7af29	max-clients	200
c264c491-701a-4dea-91f6-42d1c2dc287b	bd1c35fa-9100-4f08-a68d-1867904e6bd6	client-uris-must-match	true
4eabfbc3-a1a6-4bfa-ad5c-77d115eca709	bd1c35fa-9100-4f08-a68d-1867904e6bd6	host-sending-registration-request-must-match	true
\.


--
-- Data for Name: composite_role; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.composite_role (composite, child_role) FROM stdin;
85fd5a68-0929-4cf6-a975-a479734e3b5f	b1cdaef5-05d8-4643-8c9a-a1f88dbcce38
85fd5a68-0929-4cf6-a975-a479734e3b5f	abd96358-0cb5-4850-945e-1ac027d1f9cd
85fd5a68-0929-4cf6-a975-a479734e3b5f	7bcb5981-e18c-466e-8f4a-7043f9a175b1
85fd5a68-0929-4cf6-a975-a479734e3b5f	ba86fde9-5de1-4ff2-9963-1e3ead009381
85fd5a68-0929-4cf6-a975-a479734e3b5f	6752adef-c89e-4041-918f-4a6f4b852333
85fd5a68-0929-4cf6-a975-a479734e3b5f	54325cd3-6f42-4ae1-adf4-365395213ea1
85fd5a68-0929-4cf6-a975-a479734e3b5f	b629142a-cc37-4003-8db4-467ade17f444
85fd5a68-0929-4cf6-a975-a479734e3b5f	6e6c5210-d873-4872-8c81-cf1978d17e1c
85fd5a68-0929-4cf6-a975-a479734e3b5f	03d76b29-ad86-4a40-9e96-d15733041561
85fd5a68-0929-4cf6-a975-a479734e3b5f	fe80fd64-5e01-4bdd-af8e-b2da214c55cf
85fd5a68-0929-4cf6-a975-a479734e3b5f	cebb17b7-4ebf-4fba-bc5e-b8c7bc33806b
85fd5a68-0929-4cf6-a975-a479734e3b5f	60d8d8e0-1b7f-45cd-86a9-f273292e16a8
85fd5a68-0929-4cf6-a975-a479734e3b5f	c8867a69-42e3-4c5b-8721-98b83427f16f
85fd5a68-0929-4cf6-a975-a479734e3b5f	ab975b35-4d6c-4bbd-a1fd-f27ba06f7506
85fd5a68-0929-4cf6-a975-a479734e3b5f	795de1be-8d9c-4d43-970a-b222ef9ec7dd
85fd5a68-0929-4cf6-a975-a479734e3b5f	2cdd4831-7685-40e4-83b5-bc694384a328
85fd5a68-0929-4cf6-a975-a479734e3b5f	b2a66d6c-7f14-4344-a096-b6ee3d6ec6ee
85fd5a68-0929-4cf6-a975-a479734e3b5f	9d4c8746-5a68-4b27-9308-d170d96ce9fa
6752adef-c89e-4041-918f-4a6f4b852333	2cdd4831-7685-40e4-83b5-bc694384a328
aa39db72-82f8-431a-abc5-a5c0b1ff9db6	57e60180-30c9-42df-9be3-245c5f73be19
ba86fde9-5de1-4ff2-9963-1e3ead009381	795de1be-8d9c-4d43-970a-b222ef9ec7dd
ba86fde9-5de1-4ff2-9963-1e3ead009381	9d4c8746-5a68-4b27-9308-d170d96ce9fa
aa39db72-82f8-431a-abc5-a5c0b1ff9db6	b8d13ee3-8a59-4da6-a545-abb269a7f7c7
b8d13ee3-8a59-4da6-a545-abb269a7f7c7	ff417934-a308-4c72-85db-f3e5ae0c8191
11293df3-fdc8-4f14-afb5-b414b1b92c8d	8909c9b6-b2ef-4666-b717-ca3170ddce5c
85fd5a68-0929-4cf6-a975-a479734e3b5f	fc4dc982-3d29-4f7a-8821-9340671a9d96
aa39db72-82f8-431a-abc5-a5c0b1ff9db6	48f095eb-56dc-4d2f-ba8c-1376fb23a7ae
aa39db72-82f8-431a-abc5-a5c0b1ff9db6	05978310-6e55-4152-8508-7ddede830f44
85fd5a68-0929-4cf6-a975-a479734e3b5f	bc9ace77-0a56-4bd9-aea0-ab14b7f89d5b
85fd5a68-0929-4cf6-a975-a479734e3b5f	41e8ec6a-11e4-4f52-a2b1-662987a41f0c
85fd5a68-0929-4cf6-a975-a479734e3b5f	8bf8c5ec-ef13-4fe5-a052-4108ca30336e
85fd5a68-0929-4cf6-a975-a479734e3b5f	133a57c3-9e4f-4418-8548-e89ba80c331d
85fd5a68-0929-4cf6-a975-a479734e3b5f	534d3622-980d-4329-b17e-b53ad3428014
85fd5a68-0929-4cf6-a975-a479734e3b5f	678bc0e6-054a-42f7-ad7d-9ea4a201bdcc
85fd5a68-0929-4cf6-a975-a479734e3b5f	53237d96-fbf0-44d4-aa94-a1cd03e93ab6
85fd5a68-0929-4cf6-a975-a479734e3b5f	4eeafba1-202d-450a-a6fc-c2e9fbcd407e
85fd5a68-0929-4cf6-a975-a479734e3b5f	b6012373-27ff-4bd6-8b5c-86c36804a2ee
85fd5a68-0929-4cf6-a975-a479734e3b5f	68b257a6-fe4c-4661-bc2d-0249c445473f
85fd5a68-0929-4cf6-a975-a479734e3b5f	004a94a4-545c-42a0-a2b2-6d127b1d6eab
85fd5a68-0929-4cf6-a975-a479734e3b5f	0ce56b57-b985-43d8-94a7-c5a9ac2eb3c2
85fd5a68-0929-4cf6-a975-a479734e3b5f	8406c6b1-beb7-4e5b-a11c-9c5c1af86363
85fd5a68-0929-4cf6-a975-a479734e3b5f	74278abc-772d-44fd-85c0-fe4c3ca01d01
85fd5a68-0929-4cf6-a975-a479734e3b5f	505230c6-972f-45e3-927a-6c2bc9f1d36b
85fd5a68-0929-4cf6-a975-a479734e3b5f	1cf85f9f-b8e3-4d46-97c6-31b6fa944d24
85fd5a68-0929-4cf6-a975-a479734e3b5f	0850b683-de4a-4374-bd30-325136f64fdc
133a57c3-9e4f-4418-8548-e89ba80c331d	505230c6-972f-45e3-927a-6c2bc9f1d36b
8bf8c5ec-ef13-4fe5-a052-4108ca30336e	0850b683-de4a-4374-bd30-325136f64fdc
8bf8c5ec-ef13-4fe5-a052-4108ca30336e	74278abc-772d-44fd-85c0-fe4c3ca01d01
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	72b2ca13-d5c8-4dc5-8e68-30a2eb321ac0
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	d88c6b54-433b-48cb-8611-80a3a4b42215
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	4b04f205-d99c-4fd1-87e3-916aa8b5ae02
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	8028588a-2c42-42da-82f6-b2c1b41c0b8d
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	828d9326-474f-432d-9764-ecaf096936b4
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	cd6c9708-ca88-41d1-a02d-f2b7579ebfa4
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	e79300d5-02fd-4e89-8d8b-a66cc178f0f7
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	68baf511-fed0-4b64-ae33-900bd07c6b16
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	6019f312-99af-4b5c-8c6f-b235c6920e99
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	ae46661f-f292-4bbb-acf3-e796491ab5a3
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	1d897f99-be30-4532-8e2c-7bf06ab079a5
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	0aff7ef3-8384-49ca-aa70-8a2d02dd1690
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	7520abf1-8b78-454d-9232-fe737c673433
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	a27af109-b3d4-4af0-8de1-8397a5cdfda8
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	656d53df-6337-4315-8e11-99bc2c35851c
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	336a7990-c7b4-4bb6-a375-9202d321411f
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	54e7ac49-274e-4eb5-b8cf-65924cedf62c
4b04f205-d99c-4fd1-87e3-916aa8b5ae02	54e7ac49-274e-4eb5-b8cf-65924cedf62c
4b04f205-d99c-4fd1-87e3-916aa8b5ae02	a27af109-b3d4-4af0-8de1-8397a5cdfda8
8028588a-2c42-42da-82f6-b2c1b41c0b8d	656d53df-6337-4315-8e11-99bc2c35851c
9b8fca5f-61de-4a42-8acf-ca96ac82022c	e83bf8a4-8032-4ab4-9bd5-374146ada0d9
9b8fca5f-61de-4a42-8acf-ca96ac82022c	c12f1d5f-7b61-43d8-abe9-a2d855c196a4
c12f1d5f-7b61-43d8-abe9-a2d855c196a4	88b1bef9-d980-4cde-bd59-5f89b4056f60
c1048fbf-8c9a-45c7-ade7-01360a368238	46f2b851-6beb-4900-a772-78bc497f6fec
85fd5a68-0929-4cf6-a975-a479734e3b5f	3b718ee8-b4a5-4f32-a5e8-702b40398bb1
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	d52efe31-8ee1-4599-88de-661968ece9b7
9b8fca5f-61de-4a42-8acf-ca96ac82022c	cdeaf4d6-ddf9-4be6-9f4b-d6483659896b
9b8fca5f-61de-4a42-8acf-ca96ac82022c	1a04f8c8-3692-49bb-87c9-cddd1708c80a
\.


--
-- Data for Name: credential; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.credential (id, salt, type, user_id, created_date, user_label, secret_data, credential_data, priority) FROM stdin;
4a0ac06f-34e1-4c3e-8e0e-dd136b0b7ea1	\N	password	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133	1757916221282	\N	{"value":"P+uk5olk0/Ti/DlRvhJw547DADljybbcI4LhfpHCddQ=","salt":"+vuPXvXRZu8nYR1P5HftLg==","additionalParameters":{}}	{"hashIterations":27500,"algorithm":"pbkdf2-sha256","additionalParameters":{}}	10
e29ae25d-0f19-4b77-a68d-1ac11e3de6af	\N	password	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	1759029788127	我的密码	{"value":"9qw+5MyB9HzyOLjU5Vp5ZiKiS318D6809GQsIR+jc6c=","salt":"rLzPYEM7l6e0BQfyVT4dcQ==","additionalParameters":{}}	{"hashIterations":27500,"algorithm":"pbkdf2-sha256","additionalParameters":{}}	10
\.


--
-- Data for Name: databasechangelog; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.databasechangelog (id, author, filename, dateexecuted, orderexecuted, exectype, md5sum, description, comments, tag, liquibase, contexts, labels, deployment_id) FROM stdin;
1.0.0.Final-KEYCLOAK-5461	sthorger@redhat.com	META-INF/jpa-changelog-1.0.0.Final.xml	2025-09-15 06:03:35.365198	1	EXECUTED	9:6f1016664e21e16d26517a4418f5e3df	createTable tableName=APPLICATION_DEFAULT_ROLES; createTable tableName=CLIENT; createTable tableName=CLIENT_SESSION; createTable tableName=CLIENT_SESSION_ROLE; createTable tableName=COMPOSITE_ROLE; createTable tableName=CREDENTIAL; createTable tab...		\N	4.23.2	\N	\N	7916214219
1.0.0.Final-KEYCLOAK-5461	sthorger@redhat.com	META-INF/db2-jpa-changelog-1.0.0.Final.xml	2025-09-15 06:03:35.446117	2	MARK_RAN	9:828775b1596a07d1200ba1d49e5e3941	createTable tableName=APPLICATION_DEFAULT_ROLES; createTable tableName=CLIENT; createTable tableName=CLIENT_SESSION; createTable tableName=CLIENT_SESSION_ROLE; createTable tableName=COMPOSITE_ROLE; createTable tableName=CREDENTIAL; createTable tab...		\N	4.23.2	\N	\N	7916214219
1.1.0.Beta1	sthorger@redhat.com	META-INF/jpa-changelog-1.1.0.Beta1.xml	2025-09-15 06:03:35.540996	3	EXECUTED	9:5f090e44a7d595883c1fb61f4b41fd38	delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=CLIENT_ATTRIBUTES; createTable tableName=CLIENT_SESSION_NOTE; createTable tableName=APP_NODE_REGISTRATIONS; addColumn table...		\N	4.23.2	\N	\N	7916214219
1.1.0.Final	sthorger@redhat.com	META-INF/jpa-changelog-1.1.0.Final.xml	2025-09-15 06:03:35.554503	4	EXECUTED	9:c07e577387a3d2c04d1adc9aaad8730e	renameColumn newColumnName=EVENT_TIME, oldColumnName=TIME, tableName=EVENT_ENTITY		\N	4.23.2	\N	\N	7916214219
1.2.0.Beta1	psilva@redhat.com	META-INF/jpa-changelog-1.2.0.Beta1.xml	2025-09-15 06:03:35.747578	5	EXECUTED	9:b68ce996c655922dbcd2fe6b6ae72686	delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=PROTOCOL_MAPPER; createTable tableName=PROTOCOL_MAPPER_CONFIG; createTable tableName=...		\N	4.23.2	\N	\N	7916214219
1.2.0.Beta1	psilva@redhat.com	META-INF/db2-jpa-changelog-1.2.0.Beta1.xml	2025-09-15 06:03:35.774975	6	MARK_RAN	9:543b5c9989f024fe35c6f6c5a97de88e	delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION; createTable tableName=PROTOCOL_MAPPER; createTable tableName=PROTOCOL_MAPPER_CONFIG; createTable tableName=...		\N	4.23.2	\N	\N	7916214219
1.2.0.RC1	bburke@redhat.com	META-INF/jpa-changelog-1.2.0.CR1.xml	2025-09-15 06:03:35.934922	7	EXECUTED	9:765afebbe21cf5bbca048e632df38336	delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=MIGRATION_MODEL; createTable tableName=IDENTITY_P...		\N	4.23.2	\N	\N	7916214219
1.2.0.RC1	bburke@redhat.com	META-INF/db2-jpa-changelog-1.2.0.CR1.xml	2025-09-15 06:03:35.97177	8	MARK_RAN	9:db4a145ba11a6fdaefb397f6dbf829a1	delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=MIGRATION_MODEL; createTable tableName=IDENTITY_P...		\N	4.23.2	\N	\N	7916214219
1.2.0.Final	keycloak	META-INF/jpa-changelog-1.2.0.Final.xml	2025-09-15 06:03:35.983448	9	EXECUTED	9:9d05c7be10cdb873f8bcb41bc3a8ab23	update tableName=CLIENT; update tableName=CLIENT; update tableName=CLIENT		\N	4.23.2	\N	\N	7916214219
1.3.0	bburke@redhat.com	META-INF/jpa-changelog-1.3.0.xml	2025-09-15 06:03:36.174001	10	EXECUTED	9:18593702353128d53111f9b1ff0b82b8	delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete tableName=USER_SESSION; createTable tableName=ADMI...		\N	4.23.2	\N	\N	7916214219
1.4.0	bburke@redhat.com	META-INF/jpa-changelog-1.4.0.xml	2025-09-15 06:03:36.265239	11	EXECUTED	9:6122efe5f090e41a85c0f1c9e52cbb62	delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...		\N	4.23.2	\N	\N	7916214219
1.4.0	bburke@redhat.com	META-INF/db2-jpa-changelog-1.4.0.xml	2025-09-15 06:03:36.280231	12	MARK_RAN	9:e1ff28bf7568451453f844c5d54bb0b5	delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...		\N	4.23.2	\N	\N	7916214219
1.5.0	bburke@redhat.com	META-INF/jpa-changelog-1.5.0.xml	2025-09-15 06:03:36.30432	13	EXECUTED	9:7af32cd8957fbc069f796b61217483fd	delete tableName=CLIENT_SESSION_AUTH_STATUS; delete tableName=CLIENT_SESSION_ROLE; delete tableName=CLIENT_SESSION_PROT_MAPPER; delete tableName=CLIENT_SESSION_NOTE; delete tableName=CLIENT_SESSION; delete tableName=USER_SESSION_NOTE; delete table...		\N	4.23.2	\N	\N	7916214219
1.6.1_from15	mposolda@redhat.com	META-INF/jpa-changelog-1.6.1.xml	2025-09-15 06:03:36.346317	14	EXECUTED	9:6005e15e84714cd83226bf7879f54190	addColumn tableName=REALM; addColumn tableName=KEYCLOAK_ROLE; addColumn tableName=CLIENT; createTable tableName=OFFLINE_USER_SESSION; createTable tableName=OFFLINE_CLIENT_SESSION; addPrimaryKey constraintName=CONSTRAINT_OFFL_US_SES_PK2, tableName=...		\N	4.23.2	\N	\N	7916214219
1.6.1_from16-pre	mposolda@redhat.com	META-INF/jpa-changelog-1.6.1.xml	2025-09-15 06:03:36.350725	15	MARK_RAN	9:bf656f5a2b055d07f314431cae76f06c	delete tableName=OFFLINE_CLIENT_SESSION; delete tableName=OFFLINE_USER_SESSION		\N	4.23.2	\N	\N	7916214219
1.6.1_from16	mposolda@redhat.com	META-INF/jpa-changelog-1.6.1.xml	2025-09-15 06:03:36.358083	16	MARK_RAN	9:f8dadc9284440469dcf71e25ca6ab99b	dropPrimaryKey constraintName=CONSTRAINT_OFFLINE_US_SES_PK, tableName=OFFLINE_USER_SESSION; dropPrimaryKey constraintName=CONSTRAINT_OFFLINE_CL_SES_PK, tableName=OFFLINE_CLIENT_SESSION; addColumn tableName=OFFLINE_USER_SESSION; update tableName=OF...		\N	4.23.2	\N	\N	7916214219
1.6.1	mposolda@redhat.com	META-INF/jpa-changelog-1.6.1.xml	2025-09-15 06:03:36.364794	17	EXECUTED	9:d41d8cd98f00b204e9800998ecf8427e	empty		\N	4.23.2	\N	\N	7916214219
1.7.0	bburke@redhat.com	META-INF/jpa-changelog-1.7.0.xml	2025-09-15 06:03:36.443176	18	EXECUTED	9:3368ff0be4c2855ee2dd9ca813b38d8e	createTable tableName=KEYCLOAK_GROUP; createTable tableName=GROUP_ROLE_MAPPING; createTable tableName=GROUP_ATTRIBUTE; createTable tableName=USER_GROUP_MEMBERSHIP; createTable tableName=REALM_DEFAULT_GROUPS; addColumn tableName=IDENTITY_PROVIDER; ...		\N	4.23.2	\N	\N	7916214219
1.8.0	mposolda@redhat.com	META-INF/jpa-changelog-1.8.0.xml	2025-09-15 06:03:36.526763	19	EXECUTED	9:8ac2fb5dd030b24c0570a763ed75ed20	addColumn tableName=IDENTITY_PROVIDER; createTable tableName=CLIENT_TEMPLATE; createTable tableName=CLIENT_TEMPLATE_ATTRIBUTES; createTable tableName=TEMPLATE_SCOPE_MAPPING; dropNotNullConstraint columnName=CLIENT_ID, tableName=PROTOCOL_MAPPER; ad...		\N	4.23.2	\N	\N	7916214219
1.8.0-2	keycloak	META-INF/jpa-changelog-1.8.0.xml	2025-09-15 06:03:36.537392	20	EXECUTED	9:f91ddca9b19743db60e3057679810e6c	dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; update tableName=CREDENTIAL		\N	4.23.2	\N	\N	7916214219
1.8.0	mposolda@redhat.com	META-INF/db2-jpa-changelog-1.8.0.xml	2025-09-15 06:03:36.553301	21	MARK_RAN	9:831e82914316dc8a57dc09d755f23c51	addColumn tableName=IDENTITY_PROVIDER; createTable tableName=CLIENT_TEMPLATE; createTable tableName=CLIENT_TEMPLATE_ATTRIBUTES; createTable tableName=TEMPLATE_SCOPE_MAPPING; dropNotNullConstraint columnName=CLIENT_ID, tableName=PROTOCOL_MAPPER; ad...		\N	4.23.2	\N	\N	7916214219
1.8.0-2	keycloak	META-INF/db2-jpa-changelog-1.8.0.xml	2025-09-15 06:03:36.560545	22	MARK_RAN	9:f91ddca9b19743db60e3057679810e6c	dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; update tableName=CREDENTIAL		\N	4.23.2	\N	\N	7916214219
1.9.0	mposolda@redhat.com	META-INF/jpa-changelog-1.9.0.xml	2025-09-15 06:03:36.590262	23	EXECUTED	9:bc3d0f9e823a69dc21e23e94c7a94bb1	update tableName=REALM; update tableName=REALM; update tableName=REALM; update tableName=REALM; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=REALM; update tableName=REALM; customChange; dr...		\N	4.23.2	\N	\N	7916214219
1.9.1	keycloak	META-INF/jpa-changelog-1.9.1.xml	2025-09-15 06:03:36.601524	24	EXECUTED	9:c9999da42f543575ab790e76439a2679	modifyDataType columnName=PRIVATE_KEY, tableName=REALM; modifyDataType columnName=PUBLIC_KEY, tableName=REALM; modifyDataType columnName=CERTIFICATE, tableName=REALM		\N	4.23.2	\N	\N	7916214219
1.9.1	keycloak	META-INF/db2-jpa-changelog-1.9.1.xml	2025-09-15 06:03:36.606082	25	MARK_RAN	9:0d6c65c6f58732d81569e77b10ba301d	modifyDataType columnName=PRIVATE_KEY, tableName=REALM; modifyDataType columnName=CERTIFICATE, tableName=REALM		\N	4.23.2	\N	\N	7916214219
1.9.2	keycloak	META-INF/jpa-changelog-1.9.2.xml	2025-09-15 06:03:36.697982	26	EXECUTED	9:fc576660fc016ae53d2d4778d84d86d0	createIndex indexName=IDX_USER_EMAIL, tableName=USER_ENTITY; createIndex indexName=IDX_USER_ROLE_MAPPING, tableName=USER_ROLE_MAPPING; createIndex indexName=IDX_USER_GROUP_MAPPING, tableName=USER_GROUP_MEMBERSHIP; createIndex indexName=IDX_USER_CO...		\N	4.23.2	\N	\N	7916214219
authz-2.0.0	psilva@redhat.com	META-INF/jpa-changelog-authz-2.0.0.xml	2025-09-15 06:03:36.859509	27	EXECUTED	9:43ed6b0da89ff77206289e87eaa9c024	createTable tableName=RESOURCE_SERVER; addPrimaryKey constraintName=CONSTRAINT_FARS, tableName=RESOURCE_SERVER; addUniqueConstraint constraintName=UK_AU8TT6T700S9V50BU18WS5HA6, tableName=RESOURCE_SERVER; createTable tableName=RESOURCE_SERVER_RESOU...		\N	4.23.2	\N	\N	7916214219
authz-2.5.1	psilva@redhat.com	META-INF/jpa-changelog-authz-2.5.1.xml	2025-09-15 06:03:36.869192	28	EXECUTED	9:44bae577f551b3738740281eceb4ea70	update tableName=RESOURCE_SERVER_POLICY		\N	4.23.2	\N	\N	7916214219
2.1.0-KEYCLOAK-5461	bburke@redhat.com	META-INF/jpa-changelog-2.1.0.xml	2025-09-15 06:03:37.004965	29	EXECUTED	9:bd88e1f833df0420b01e114533aee5e8	createTable tableName=BROKER_LINK; createTable tableName=FED_USER_ATTRIBUTE; createTable tableName=FED_USER_CONSENT; createTable tableName=FED_USER_CONSENT_ROLE; createTable tableName=FED_USER_CONSENT_PROT_MAPPER; createTable tableName=FED_USER_CR...		\N	4.23.2	\N	\N	7916214219
2.2.0	bburke@redhat.com	META-INF/jpa-changelog-2.2.0.xml	2025-09-15 06:03:37.03569	30	EXECUTED	9:a7022af5267f019d020edfe316ef4371	addColumn tableName=ADMIN_EVENT_ENTITY; createTable tableName=CREDENTIAL_ATTRIBUTE; createTable tableName=FED_CREDENTIAL_ATTRIBUTE; modifyDataType columnName=VALUE, tableName=CREDENTIAL; addForeignKeyConstraint baseTableName=FED_CREDENTIAL_ATTRIBU...		\N	4.23.2	\N	\N	7916214219
2.3.0	bburke@redhat.com	META-INF/jpa-changelog-2.3.0.xml	2025-09-15 06:03:37.073636	31	EXECUTED	9:fc155c394040654d6a79227e56f5e25a	createTable tableName=FEDERATED_USER; addPrimaryKey constraintName=CONSTR_FEDERATED_USER, tableName=FEDERATED_USER; dropDefaultValue columnName=TOTP, tableName=USER_ENTITY; dropColumn columnName=TOTP, tableName=USER_ENTITY; addColumn tableName=IDE...		\N	4.23.2	\N	\N	7916214219
2.4.0	bburke@redhat.com	META-INF/jpa-changelog-2.4.0.xml	2025-09-15 06:03:37.080968	32	EXECUTED	9:eac4ffb2a14795e5dc7b426063e54d88	customChange		\N	4.23.2	\N	\N	7916214219
2.5.0	bburke@redhat.com	META-INF/jpa-changelog-2.5.0.xml	2025-09-15 06:03:37.089559	33	EXECUTED	9:54937c05672568c4c64fc9524c1e9462	customChange; modifyDataType columnName=USER_ID, tableName=OFFLINE_USER_SESSION		\N	4.23.2	\N	\N	7916214219
2.5.0-unicode-oracle	hmlnarik@redhat.com	META-INF/jpa-changelog-2.5.0.xml	2025-09-15 06:03:37.095352	34	MARK_RAN	9:3a32bace77c84d7678d035a7f5a8084e	modifyDataType columnName=DESCRIPTION, tableName=AUTHENTICATION_FLOW; modifyDataType columnName=DESCRIPTION, tableName=CLIENT_TEMPLATE; modifyDataType columnName=DESCRIPTION, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=DESCRIPTION,...		\N	4.23.2	\N	\N	7916214219
2.5.0-unicode-other-dbs	hmlnarik@redhat.com	META-INF/jpa-changelog-2.5.0.xml	2025-09-15 06:03:37.144125	35	EXECUTED	9:33d72168746f81f98ae3a1e8e0ca3554	modifyDataType columnName=DESCRIPTION, tableName=AUTHENTICATION_FLOW; modifyDataType columnName=DESCRIPTION, tableName=CLIENT_TEMPLATE; modifyDataType columnName=DESCRIPTION, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=DESCRIPTION,...		\N	4.23.2	\N	\N	7916214219
2.5.0-duplicate-email-support	slawomir@dabek.name	META-INF/jpa-changelog-2.5.0.xml	2025-09-15 06:03:37.154575	36	EXECUTED	9:61b6d3d7a4c0e0024b0c839da283da0c	addColumn tableName=REALM		\N	4.23.2	\N	\N	7916214219
2.5.0-unique-group-names	hmlnarik@redhat.com	META-INF/jpa-changelog-2.5.0.xml	2025-09-15 06:03:37.171365	37	EXECUTED	9:8dcac7bdf7378e7d823cdfddebf72fda	addUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP		\N	4.23.2	\N	\N	7916214219
2.5.1	bburke@redhat.com	META-INF/jpa-changelog-2.5.1.xml	2025-09-15 06:03:37.183557	38	EXECUTED	9:a2b870802540cb3faa72098db5388af3	addColumn tableName=FED_USER_CONSENT		\N	4.23.2	\N	\N	7916214219
3.0.0	bburke@redhat.com	META-INF/jpa-changelog-3.0.0.xml	2025-09-15 06:03:37.194148	39	EXECUTED	9:132a67499ba24bcc54fb5cbdcfe7e4c0	addColumn tableName=IDENTITY_PROVIDER		\N	4.23.2	\N	\N	7916214219
3.2.0-fix	keycloak	META-INF/jpa-changelog-3.2.0.xml	2025-09-15 06:03:37.198361	40	MARK_RAN	9:938f894c032f5430f2b0fafb1a243462	addNotNullConstraint columnName=REALM_ID, tableName=CLIENT_INITIAL_ACCESS		\N	4.23.2	\N	\N	7916214219
3.2.0-fix-with-keycloak-5416	keycloak	META-INF/jpa-changelog-3.2.0.xml	2025-09-15 06:03:37.204207	41	MARK_RAN	9:845c332ff1874dc5d35974b0babf3006	dropIndex indexName=IDX_CLIENT_INIT_ACC_REALM, tableName=CLIENT_INITIAL_ACCESS; addNotNullConstraint columnName=REALM_ID, tableName=CLIENT_INITIAL_ACCESS; createIndex indexName=IDX_CLIENT_INIT_ACC_REALM, tableName=CLIENT_INITIAL_ACCESS		\N	4.23.2	\N	\N	7916214219
3.2.0-fix-offline-sessions	hmlnarik	META-INF/jpa-changelog-3.2.0.xml	2025-09-15 06:03:37.210188	42	EXECUTED	9:fc86359c079781adc577c5a217e4d04c	customChange		\N	4.23.2	\N	\N	7916214219
3.2.0-fixed	keycloak	META-INF/jpa-changelog-3.2.0.xml	2025-09-15 06:03:37.541481	43	EXECUTED	9:59a64800e3c0d09b825f8a3b444fa8f4	addColumn tableName=REALM; dropPrimaryKey constraintName=CONSTRAINT_OFFL_CL_SES_PK2, tableName=OFFLINE_CLIENT_SESSION; dropColumn columnName=CLIENT_SESSION_ID, tableName=OFFLINE_CLIENT_SESSION; addPrimaryKey constraintName=CONSTRAINT_OFFL_CL_SES_P...		\N	4.23.2	\N	\N	7916214219
3.3.0	keycloak	META-INF/jpa-changelog-3.3.0.xml	2025-09-15 06:03:37.551448	44	EXECUTED	9:d48d6da5c6ccf667807f633fe489ce88	addColumn tableName=USER_ENTITY		\N	4.23.2	\N	\N	7916214219
authz-3.4.0.CR1-resource-server-pk-change-part1	glavoie@gmail.com	META-INF/jpa-changelog-authz-3.4.0.CR1.xml	2025-09-15 06:03:37.561589	45	EXECUTED	9:dde36f7973e80d71fceee683bc5d2951	addColumn tableName=RESOURCE_SERVER_POLICY; addColumn tableName=RESOURCE_SERVER_RESOURCE; addColumn tableName=RESOURCE_SERVER_SCOPE		\N	4.23.2	\N	\N	7916214219
authz-3.4.0.CR1-resource-server-pk-change-part2-KEYCLOAK-6095	hmlnarik@redhat.com	META-INF/jpa-changelog-authz-3.4.0.CR1.xml	2025-09-15 06:03:37.568362	46	EXECUTED	9:b855e9b0a406b34fa323235a0cf4f640	customChange		\N	4.23.2	\N	\N	7916214219
authz-3.4.0.CR1-resource-server-pk-change-part3-fixed	glavoie@gmail.com	META-INF/jpa-changelog-authz-3.4.0.CR1.xml	2025-09-15 06:03:37.571726	47	MARK_RAN	9:51abbacd7b416c50c4421a8cabf7927e	dropIndex indexName=IDX_RES_SERV_POL_RES_SERV, tableName=RESOURCE_SERVER_POLICY; dropIndex indexName=IDX_RES_SRV_RES_RES_SRV, tableName=RESOURCE_SERVER_RESOURCE; dropIndex indexName=IDX_RES_SRV_SCOPE_RES_SRV, tableName=RESOURCE_SERVER_SCOPE		\N	4.23.2	\N	\N	7916214219
authz-3.4.0.CR1-resource-server-pk-change-part3-fixed-nodropindex	glavoie@gmail.com	META-INF/jpa-changelog-authz-3.4.0.CR1.xml	2025-09-15 06:03:37.649732	48	EXECUTED	9:bdc99e567b3398bac83263d375aad143	addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, tableName=RESOURCE_SERVER_POLICY; addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, tableName=RESOURCE_SERVER_RESOURCE; addNotNullConstraint columnName=RESOURCE_SERVER_CLIENT_ID, ...		\N	4.23.2	\N	\N	7916214219
authn-3.4.0.CR1-refresh-token-max-reuse	glavoie@gmail.com	META-INF/jpa-changelog-authz-3.4.0.CR1.xml	2025-09-15 06:03:37.659333	49	EXECUTED	9:d198654156881c46bfba39abd7769e69	addColumn tableName=REALM		\N	4.23.2	\N	\N	7916214219
3.4.0	keycloak	META-INF/jpa-changelog-3.4.0.xml	2025-09-15 06:03:37.769198	50	EXECUTED	9:cfdd8736332ccdd72c5256ccb42335db	addPrimaryKey constraintName=CONSTRAINT_REALM_DEFAULT_ROLES, tableName=REALM_DEFAULT_ROLES; addPrimaryKey constraintName=CONSTRAINT_COMPOSITE_ROLE, tableName=COMPOSITE_ROLE; addPrimaryKey constraintName=CONSTR_REALM_DEFAULT_GROUPS, tableName=REALM...		\N	4.23.2	\N	\N	7916214219
3.4.0-KEYCLOAK-5230	hmlnarik@redhat.com	META-INF/jpa-changelog-3.4.0.xml	2025-09-15 06:03:37.858462	51	EXECUTED	9:7c84de3d9bd84d7f077607c1a4dcb714	createIndex indexName=IDX_FU_ATTRIBUTE, tableName=FED_USER_ATTRIBUTE; createIndex indexName=IDX_FU_CONSENT, tableName=FED_USER_CONSENT; createIndex indexName=IDX_FU_CONSENT_RU, tableName=FED_USER_CONSENT; createIndex indexName=IDX_FU_CREDENTIAL, t...		\N	4.23.2	\N	\N	7916214219
3.4.1	psilva@redhat.com	META-INF/jpa-changelog-3.4.1.xml	2025-09-15 06:03:37.865622	52	EXECUTED	9:5a6bb36cbefb6a9d6928452c0852af2d	modifyDataType columnName=VALUE, tableName=CLIENT_ATTRIBUTES		\N	4.23.2	\N	\N	7916214219
3.4.2	keycloak	META-INF/jpa-changelog-3.4.2.xml	2025-09-15 06:03:37.873505	53	EXECUTED	9:8f23e334dbc59f82e0a328373ca6ced0	update tableName=REALM		\N	4.23.2	\N	\N	7916214219
3.4.2-KEYCLOAK-5172	mkanis@redhat.com	META-INF/jpa-changelog-3.4.2.xml	2025-09-15 06:03:37.879192	54	EXECUTED	9:9156214268f09d970cdf0e1564d866af	update tableName=CLIENT		\N	4.23.2	\N	\N	7916214219
4.0.0-KEYCLOAK-6335	bburke@redhat.com	META-INF/jpa-changelog-4.0.0.xml	2025-09-15 06:03:37.891385	55	EXECUTED	9:db806613b1ed154826c02610b7dbdf74	createTable tableName=CLIENT_AUTH_FLOW_BINDINGS; addPrimaryKey constraintName=C_CLI_FLOW_BIND, tableName=CLIENT_AUTH_FLOW_BINDINGS		\N	4.23.2	\N	\N	7916214219
4.0.0-CLEANUP-UNUSED-TABLE	bburke@redhat.com	META-INF/jpa-changelog-4.0.0.xml	2025-09-15 06:03:37.899925	56	EXECUTED	9:229a041fb72d5beac76bb94a5fa709de	dropTable tableName=CLIENT_IDENTITY_PROV_MAPPING		\N	4.23.2	\N	\N	7916214219
4.0.0-KEYCLOAK-6228	bburke@redhat.com	META-INF/jpa-changelog-4.0.0.xml	2025-09-15 06:03:37.951217	57	EXECUTED	9:079899dade9c1e683f26b2aa9ca6ff04	dropUniqueConstraint constraintName=UK_JKUWUVD56ONTGSUHOGM8UEWRT, tableName=USER_CONSENT; dropNotNullConstraint columnName=CLIENT_ID, tableName=USER_CONSENT; addColumn tableName=USER_CONSENT; addUniqueConstraint constraintName=UK_JKUWUVD56ONTGSUHO...		\N	4.23.2	\N	\N	7916214219
4.0.0-KEYCLOAK-5579-fixed	mposolda@redhat.com	META-INF/jpa-changelog-4.0.0.xml	2025-09-15 06:03:38.112721	58	EXECUTED	9:139b79bcbbfe903bb1c2d2a4dbf001d9	dropForeignKeyConstraint baseTableName=CLIENT_TEMPLATE_ATTRIBUTES, constraintName=FK_CL_TEMPL_ATTR_TEMPL; renameTable newTableName=CLIENT_SCOPE_ATTRIBUTES, oldTableName=CLIENT_TEMPLATE_ATTRIBUTES; renameColumn newColumnName=SCOPE_ID, oldColumnName...		\N	4.23.2	\N	\N	7916214219
authz-4.0.0.CR1	psilva@redhat.com	META-INF/jpa-changelog-authz-4.0.0.CR1.xml	2025-09-15 06:03:38.168204	59	EXECUTED	9:b55738ad889860c625ba2bf483495a04	createTable tableName=RESOURCE_SERVER_PERM_TICKET; addPrimaryKey constraintName=CONSTRAINT_FAPMT, tableName=RESOURCE_SERVER_PERM_TICKET; addForeignKeyConstraint baseTableName=RESOURCE_SERVER_PERM_TICKET, constraintName=FK_FRSRHO213XCX4WNKOG82SSPMT...		\N	4.23.2	\N	\N	7916214219
authz-4.0.0.Beta3	psilva@redhat.com	META-INF/jpa-changelog-authz-4.0.0.Beta3.xml	2025-09-15 06:03:38.180132	60	EXECUTED	9:e0057eac39aa8fc8e09ac6cfa4ae15fe	addColumn tableName=RESOURCE_SERVER_POLICY; addColumn tableName=RESOURCE_SERVER_PERM_TICKET; addForeignKeyConstraint baseTableName=RESOURCE_SERVER_PERM_TICKET, constraintName=FK_FRSRPO2128CX4WNKOG82SSRFY, referencedTableName=RESOURCE_SERVER_POLICY		\N	4.23.2	\N	\N	7916214219
authz-4.2.0.Final	mhajas@redhat.com	META-INF/jpa-changelog-authz-4.2.0.Final.xml	2025-09-15 06:03:38.200357	61	EXECUTED	9:42a33806f3a0443fe0e7feeec821326c	createTable tableName=RESOURCE_URIS; addForeignKeyConstraint baseTableName=RESOURCE_URIS, constraintName=FK_RESOURCE_SERVER_URIS, referencedTableName=RESOURCE_SERVER_RESOURCE; customChange; dropColumn columnName=URI, tableName=RESOURCE_SERVER_RESO...		\N	4.23.2	\N	\N	7916214219
authz-4.2.0.Final-KEYCLOAK-9944	hmlnarik@redhat.com	META-INF/jpa-changelog-authz-4.2.0.Final.xml	2025-09-15 06:03:38.216141	62	EXECUTED	9:9968206fca46eecc1f51db9c024bfe56	addPrimaryKey constraintName=CONSTRAINT_RESOUR_URIS_PK, tableName=RESOURCE_URIS		\N	4.23.2	\N	\N	7916214219
4.2.0-KEYCLOAK-6313	wadahiro@gmail.com	META-INF/jpa-changelog-4.2.0.xml	2025-09-15 06:03:38.223694	63	EXECUTED	9:92143a6daea0a3f3b8f598c97ce55c3d	addColumn tableName=REQUIRED_ACTION_PROVIDER		\N	4.23.2	\N	\N	7916214219
4.3.0-KEYCLOAK-7984	wadahiro@gmail.com	META-INF/jpa-changelog-4.3.0.xml	2025-09-15 06:03:38.229841	64	EXECUTED	9:82bab26a27195d889fb0429003b18f40	update tableName=REQUIRED_ACTION_PROVIDER		\N	4.23.2	\N	\N	7916214219
4.6.0-KEYCLOAK-7950	psilva@redhat.com	META-INF/jpa-changelog-4.6.0.xml	2025-09-15 06:03:38.235634	65	EXECUTED	9:e590c88ddc0b38b0ae4249bbfcb5abc3	update tableName=RESOURCE_SERVER_RESOURCE		\N	4.23.2	\N	\N	7916214219
4.6.0-KEYCLOAK-8377	keycloak	META-INF/jpa-changelog-4.6.0.xml	2025-09-15 06:03:38.273902	66	EXECUTED	9:5c1f475536118dbdc38d5d7977950cc0	createTable tableName=ROLE_ATTRIBUTE; addPrimaryKey constraintName=CONSTRAINT_ROLE_ATTRIBUTE_PK, tableName=ROLE_ATTRIBUTE; addForeignKeyConstraint baseTableName=ROLE_ATTRIBUTE, constraintName=FK_ROLE_ATTRIBUTE_ID, referencedTableName=KEYCLOAK_ROLE...		\N	4.23.2	\N	\N	7916214219
4.6.0-KEYCLOAK-8555	gideonray@gmail.com	META-INF/jpa-changelog-4.6.0.xml	2025-09-15 06:03:38.293552	67	EXECUTED	9:e7c9f5f9c4d67ccbbcc215440c718a17	createIndex indexName=IDX_COMPONENT_PROVIDER_TYPE, tableName=COMPONENT		\N	4.23.2	\N	\N	7916214219
4.7.0-KEYCLOAK-1267	sguilhen@redhat.com	META-INF/jpa-changelog-4.7.0.xml	2025-09-15 06:03:38.301604	68	EXECUTED	9:88e0bfdda924690d6f4e430c53447dd5	addColumn tableName=REALM		\N	4.23.2	\N	\N	7916214219
4.7.0-KEYCLOAK-7275	keycloak	META-INF/jpa-changelog-4.7.0.xml	2025-09-15 06:03:38.328617	69	EXECUTED	9:f53177f137e1c46b6a88c59ec1cb5218	renameColumn newColumnName=CREATED_ON, oldColumnName=LAST_SESSION_REFRESH, tableName=OFFLINE_USER_SESSION; addNotNullConstraint columnName=CREATED_ON, tableName=OFFLINE_USER_SESSION; addColumn tableName=OFFLINE_USER_SESSION; customChange; createIn...		\N	4.23.2	\N	\N	7916214219
4.8.0-KEYCLOAK-8835	sguilhen@redhat.com	META-INF/jpa-changelog-4.8.0.xml	2025-09-15 06:03:38.339234	70	EXECUTED	9:a74d33da4dc42a37ec27121580d1459f	addNotNullConstraint columnName=SSO_MAX_LIFESPAN_REMEMBER_ME, tableName=REALM; addNotNullConstraint columnName=SSO_IDLE_TIMEOUT_REMEMBER_ME, tableName=REALM		\N	4.23.2	\N	\N	7916214219
authz-7.0.0-KEYCLOAK-10443	psilva@redhat.com	META-INF/jpa-changelog-authz-7.0.0.xml	2025-09-15 06:03:38.345558	71	EXECUTED	9:fd4ade7b90c3b67fae0bfcfcb42dfb5f	addColumn tableName=RESOURCE_SERVER		\N	4.23.2	\N	\N	7916214219
8.0.0-adding-credential-columns	keycloak	META-INF/jpa-changelog-8.0.0.xml	2025-09-15 06:03:38.357779	72	EXECUTED	9:aa072ad090bbba210d8f18781b8cebf4	addColumn tableName=CREDENTIAL; addColumn tableName=FED_USER_CREDENTIAL		\N	4.23.2	\N	\N	7916214219
8.0.0-updating-credential-data-not-oracle-fixed	keycloak	META-INF/jpa-changelog-8.0.0.xml	2025-09-15 06:03:38.36699	73	EXECUTED	9:1ae6be29bab7c2aa376f6983b932be37	update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL		\N	4.23.2	\N	\N	7916214219
8.0.0-updating-credential-data-oracle-fixed	keycloak	META-INF/jpa-changelog-8.0.0.xml	2025-09-15 06:03:38.370691	74	MARK_RAN	9:14706f286953fc9a25286dbd8fb30d97	update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL; update tableName=FED_USER_CREDENTIAL		\N	4.23.2	\N	\N	7916214219
8.0.0-credential-cleanup-fixed	keycloak	META-INF/jpa-changelog-8.0.0.xml	2025-09-15 06:03:38.390646	75	EXECUTED	9:2b9cc12779be32c5b40e2e67711a218b	dropDefaultValue columnName=COUNTER, tableName=CREDENTIAL; dropDefaultValue columnName=DIGITS, tableName=CREDENTIAL; dropDefaultValue columnName=PERIOD, tableName=CREDENTIAL; dropDefaultValue columnName=ALGORITHM, tableName=CREDENTIAL; dropColumn ...		\N	4.23.2	\N	\N	7916214219
8.0.0-resource-tag-support	keycloak	META-INF/jpa-changelog-8.0.0.xml	2025-09-15 06:03:38.409679	76	EXECUTED	9:91fa186ce7a5af127a2d7a91ee083cc5	addColumn tableName=MIGRATION_MODEL; createIndex indexName=IDX_UPDATE_TIME, tableName=MIGRATION_MODEL		\N	4.23.2	\N	\N	7916214219
9.0.0-always-display-client	keycloak	META-INF/jpa-changelog-9.0.0.xml	2025-09-15 06:03:38.419701	77	EXECUTED	9:6335e5c94e83a2639ccd68dd24e2e5ad	addColumn tableName=CLIENT		\N	4.23.2	\N	\N	7916214219
9.0.0-drop-constraints-for-column-increase	keycloak	META-INF/jpa-changelog-9.0.0.xml	2025-09-15 06:03:38.423941	78	MARK_RAN	9:6bdb5658951e028bfe16fa0a8228b530	dropUniqueConstraint constraintName=UK_FRSR6T700S9V50BU18WS5PMT, tableName=RESOURCE_SERVER_PERM_TICKET; dropUniqueConstraint constraintName=UK_FRSR6T700S9V50BU18WS5HA6, tableName=RESOURCE_SERVER_RESOURCE; dropPrimaryKey constraintName=CONSTRAINT_O...		\N	4.23.2	\N	\N	7916214219
9.0.0-increase-column-size-federated-fk	keycloak	META-INF/jpa-changelog-9.0.0.xml	2025-09-15 06:03:38.452244	79	EXECUTED	9:d5bc15a64117ccad481ce8792d4c608f	modifyDataType columnName=CLIENT_ID, tableName=FED_USER_CONSENT; modifyDataType columnName=CLIENT_REALM_CONSTRAINT, tableName=KEYCLOAK_ROLE; modifyDataType columnName=OWNER, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=CLIENT_ID, ta...		\N	4.23.2	\N	\N	7916214219
9.0.0-recreate-constraints-after-column-increase	keycloak	META-INF/jpa-changelog-9.0.0.xml	2025-09-15 06:03:38.459854	80	MARK_RAN	9:077cba51999515f4d3e7ad5619ab592c	addNotNullConstraint columnName=CLIENT_ID, tableName=OFFLINE_CLIENT_SESSION; addNotNullConstraint columnName=OWNER, tableName=RESOURCE_SERVER_PERM_TICKET; addNotNullConstraint columnName=REQUESTER, tableName=RESOURCE_SERVER_PERM_TICKET; addNotNull...		\N	4.23.2	\N	\N	7916214219
9.0.1-add-index-to-client.client_id	keycloak	META-INF/jpa-changelog-9.0.1.xml	2025-09-15 06:03:38.478891	81	EXECUTED	9:be969f08a163bf47c6b9e9ead8ac2afb	createIndex indexName=IDX_CLIENT_ID, tableName=CLIENT		\N	4.23.2	\N	\N	7916214219
9.0.1-KEYCLOAK-12579-drop-constraints	keycloak	META-INF/jpa-changelog-9.0.1.xml	2025-09-15 06:03:38.484331	82	MARK_RAN	9:6d3bb4408ba5a72f39bd8a0b301ec6e3	dropUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP		\N	4.23.2	\N	\N	7916214219
9.0.1-KEYCLOAK-12579-add-not-null-constraint	keycloak	META-INF/jpa-changelog-9.0.1.xml	2025-09-15 06:03:38.496069	83	EXECUTED	9:966bda61e46bebf3cc39518fbed52fa7	addNotNullConstraint columnName=PARENT_GROUP, tableName=KEYCLOAK_GROUP		\N	4.23.2	\N	\N	7916214219
9.0.1-KEYCLOAK-12579-recreate-constraints	keycloak	META-INF/jpa-changelog-9.0.1.xml	2025-09-15 06:03:38.500754	84	MARK_RAN	9:8dcac7bdf7378e7d823cdfddebf72fda	addUniqueConstraint constraintName=SIBLING_NAMES, tableName=KEYCLOAK_GROUP		\N	4.23.2	\N	\N	7916214219
9.0.1-add-index-to-events	keycloak	META-INF/jpa-changelog-9.0.1.xml	2025-09-15 06:03:38.515533	85	EXECUTED	9:7d93d602352a30c0c317e6a609b56599	createIndex indexName=IDX_EVENT_TIME, tableName=EVENT_ENTITY		\N	4.23.2	\N	\N	7916214219
map-remove-ri	keycloak	META-INF/jpa-changelog-11.0.0.xml	2025-09-15 06:03:38.525527	86	EXECUTED	9:71c5969e6cdd8d7b6f47cebc86d37627	dropForeignKeyConstraint baseTableName=REALM, constraintName=FK_TRAF444KK6QRKMS7N56AIWQ5Y; dropForeignKeyConstraint baseTableName=KEYCLOAK_ROLE, constraintName=FK_KJHO5LE2C0RAL09FL8CM9WFW9		\N	4.23.2	\N	\N	7916214219
map-remove-ri	keycloak	META-INF/jpa-changelog-12.0.0.xml	2025-09-15 06:03:38.540146	87	EXECUTED	9:a9ba7d47f065f041b7da856a81762021	dropForeignKeyConstraint baseTableName=REALM_DEFAULT_GROUPS, constraintName=FK_DEF_GROUPS_GROUP; dropForeignKeyConstraint baseTableName=REALM_DEFAULT_ROLES, constraintName=FK_H4WPD7W4HSOOLNI3H0SW7BTJE; dropForeignKeyConstraint baseTableName=CLIENT...		\N	4.23.2	\N	\N	7916214219
12.1.0-add-realm-localization-table	keycloak	META-INF/jpa-changelog-12.0.0.xml	2025-09-15 06:03:38.570268	88	EXECUTED	9:fffabce2bc01e1a8f5110d5278500065	createTable tableName=REALM_LOCALIZATIONS; addPrimaryKey tableName=REALM_LOCALIZATIONS		\N	4.23.2	\N	\N	7916214219
default-roles	keycloak	META-INF/jpa-changelog-13.0.0.xml	2025-09-15 06:03:38.579383	89	EXECUTED	9:fa8a5b5445e3857f4b010bafb5009957	addColumn tableName=REALM; customChange		\N	4.23.2	\N	\N	7916214219
default-roles-cleanup	keycloak	META-INF/jpa-changelog-13.0.0.xml	2025-09-15 06:03:38.588163	90	EXECUTED	9:67ac3241df9a8582d591c5ed87125f39	dropTable tableName=REALM_DEFAULT_ROLES; dropTable tableName=CLIENT_DEFAULT_ROLES		\N	4.23.2	\N	\N	7916214219
13.0.0-KEYCLOAK-16844	keycloak	META-INF/jpa-changelog-13.0.0.xml	2025-09-15 06:03:38.599595	91	EXECUTED	9:ad1194d66c937e3ffc82386c050ba089	createIndex indexName=IDX_OFFLINE_USS_PRELOAD, tableName=OFFLINE_USER_SESSION		\N	4.23.2	\N	\N	7916214219
map-remove-ri-13.0.0	keycloak	META-INF/jpa-changelog-13.0.0.xml	2025-09-15 06:03:38.611788	92	EXECUTED	9:d9be619d94af5a2f5d07b9f003543b91	dropForeignKeyConstraint baseTableName=DEFAULT_CLIENT_SCOPE, constraintName=FK_R_DEF_CLI_SCOPE_SCOPE; dropForeignKeyConstraint baseTableName=CLIENT_SCOPE_CLIENT, constraintName=FK_C_CLI_SCOPE_SCOPE; dropForeignKeyConstraint baseTableName=CLIENT_SC...		\N	4.23.2	\N	\N	7916214219
13.0.0-KEYCLOAK-17992-drop-constraints	keycloak	META-INF/jpa-changelog-13.0.0.xml	2025-09-15 06:03:38.616761	93	MARK_RAN	9:544d201116a0fcc5a5da0925fbbc3bde	dropPrimaryKey constraintName=C_CLI_SCOPE_BIND, tableName=CLIENT_SCOPE_CLIENT; dropIndex indexName=IDX_CLSCOPE_CL, tableName=CLIENT_SCOPE_CLIENT; dropIndex indexName=IDX_CL_CLSCOPE, tableName=CLIENT_SCOPE_CLIENT		\N	4.23.2	\N	\N	7916214219
13.0.0-increase-column-size-federated	keycloak	META-INF/jpa-changelog-13.0.0.xml	2025-09-15 06:03:38.637796	94	EXECUTED	9:43c0c1055b6761b4b3e89de76d612ccf	modifyDataType columnName=CLIENT_ID, tableName=CLIENT_SCOPE_CLIENT; modifyDataType columnName=SCOPE_ID, tableName=CLIENT_SCOPE_CLIENT		\N	4.23.2	\N	\N	7916214219
13.0.0-KEYCLOAK-17992-recreate-constraints	keycloak	META-INF/jpa-changelog-13.0.0.xml	2025-09-15 06:03:38.643333	95	MARK_RAN	9:8bd711fd0330f4fe980494ca43ab1139	addNotNullConstraint columnName=CLIENT_ID, tableName=CLIENT_SCOPE_CLIENT; addNotNullConstraint columnName=SCOPE_ID, tableName=CLIENT_SCOPE_CLIENT; addPrimaryKey constraintName=C_CLI_SCOPE_BIND, tableName=CLIENT_SCOPE_CLIENT; createIndex indexName=...		\N	4.23.2	\N	\N	7916214219
json-string-accomodation-fixed	keycloak	META-INF/jpa-changelog-13.0.0.xml	2025-09-15 06:03:38.653842	96	EXECUTED	9:e07d2bc0970c348bb06fb63b1f82ddbf	addColumn tableName=REALM_ATTRIBUTE; update tableName=REALM_ATTRIBUTE; dropColumn columnName=VALUE, tableName=REALM_ATTRIBUTE; renameColumn newColumnName=VALUE, oldColumnName=VALUE_NEW, tableName=REALM_ATTRIBUTE		\N	4.23.2	\N	\N	7916214219
14.0.0-KEYCLOAK-11019	keycloak	META-INF/jpa-changelog-14.0.0.xml	2025-09-15 06:03:38.680012	97	EXECUTED	9:24fb8611e97f29989bea412aa38d12b7	createIndex indexName=IDX_OFFLINE_CSS_PRELOAD, tableName=OFFLINE_CLIENT_SESSION; createIndex indexName=IDX_OFFLINE_USS_BY_USER, tableName=OFFLINE_USER_SESSION; createIndex indexName=IDX_OFFLINE_USS_BY_USERSESS, tableName=OFFLINE_USER_SESSION		\N	4.23.2	\N	\N	7916214219
14.0.0-KEYCLOAK-18286	keycloak	META-INF/jpa-changelog-14.0.0.xml	2025-09-15 06:03:38.684537	98	MARK_RAN	9:259f89014ce2506ee84740cbf7163aa7	createIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES		\N	4.23.2	\N	\N	7916214219
14.0.0-KEYCLOAK-18286-revert	keycloak	META-INF/jpa-changelog-14.0.0.xml	2025-09-15 06:03:38.699738	99	MARK_RAN	9:04baaf56c116ed19951cbc2cca584022	dropIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES		\N	4.23.2	\N	\N	7916214219
14.0.0-KEYCLOAK-18286-supported-dbs	keycloak	META-INF/jpa-changelog-14.0.0.xml	2025-09-15 06:03:38.716124	100	EXECUTED	9:60ca84a0f8c94ec8c3504a5a3bc88ee8	createIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES		\N	4.23.2	\N	\N	7916214219
14.0.0-KEYCLOAK-18286-unsupported-dbs	keycloak	META-INF/jpa-changelog-14.0.0.xml	2025-09-15 06:03:38.720455	101	MARK_RAN	9:d3d977031d431db16e2c181ce49d73e9	createIndex indexName=IDX_CLIENT_ATT_BY_NAME_VALUE, tableName=CLIENT_ATTRIBUTES		\N	4.23.2	\N	\N	7916214219
KEYCLOAK-17267-add-index-to-user-attributes	keycloak	META-INF/jpa-changelog-14.0.0.xml	2025-09-15 06:03:38.733399	102	EXECUTED	9:0b305d8d1277f3a89a0a53a659ad274c	createIndex indexName=IDX_USER_ATTRIBUTE_NAME, tableName=USER_ATTRIBUTE		\N	4.23.2	\N	\N	7916214219
KEYCLOAK-18146-add-saml-art-binding-identifier	keycloak	META-INF/jpa-changelog-14.0.0.xml	2025-09-15 06:03:38.742454	103	EXECUTED	9:2c374ad2cdfe20e2905a84c8fac48460	customChange		\N	4.23.2	\N	\N	7916214219
15.0.0-KEYCLOAK-18467	keycloak	META-INF/jpa-changelog-15.0.0.xml	2025-09-15 06:03:38.753722	104	EXECUTED	9:47a760639ac597360a8219f5b768b4de	addColumn tableName=REALM_LOCALIZATIONS; update tableName=REALM_LOCALIZATIONS; dropColumn columnName=TEXTS, tableName=REALM_LOCALIZATIONS; renameColumn newColumnName=TEXTS, oldColumnName=TEXTS_NEW, tableName=REALM_LOCALIZATIONS; addNotNullConstrai...		\N	4.23.2	\N	\N	7916214219
17.0.0-9562	keycloak	META-INF/jpa-changelog-17.0.0.xml	2025-09-15 06:03:38.770648	105	EXECUTED	9:a6272f0576727dd8cad2522335f5d99e	createIndex indexName=IDX_USER_SERVICE_ACCOUNT, tableName=USER_ENTITY		\N	4.23.2	\N	\N	7916214219
18.0.0-10625-IDX_ADMIN_EVENT_TIME	keycloak	META-INF/jpa-changelog-18.0.0.xml	2025-09-15 06:03:38.787947	106	EXECUTED	9:015479dbd691d9cc8669282f4828c41d	createIndex indexName=IDX_ADMIN_EVENT_TIME, tableName=ADMIN_EVENT_ENTITY		\N	4.23.2	\N	\N	7916214219
19.0.0-10135	keycloak	META-INF/jpa-changelog-19.0.0.xml	2025-09-15 06:03:38.795189	107	EXECUTED	9:9518e495fdd22f78ad6425cc30630221	customChange		\N	4.23.2	\N	\N	7916214219
20.0.0-12964-supported-dbs	keycloak	META-INF/jpa-changelog-20.0.0.xml	2025-09-15 06:03:38.805488	108	EXECUTED	9:e5f243877199fd96bcc842f27a1656ac	createIndex indexName=IDX_GROUP_ATT_BY_NAME_VALUE, tableName=GROUP_ATTRIBUTE		\N	4.23.2	\N	\N	7916214219
20.0.0-12964-unsupported-dbs	keycloak	META-INF/jpa-changelog-20.0.0.xml	2025-09-15 06:03:38.809959	109	MARK_RAN	9:1a6fcaa85e20bdeae0a9ce49b41946a5	createIndex indexName=IDX_GROUP_ATT_BY_NAME_VALUE, tableName=GROUP_ATTRIBUTE		\N	4.23.2	\N	\N	7916214219
client-attributes-string-accomodation-fixed	keycloak	META-INF/jpa-changelog-20.0.0.xml	2025-09-15 06:03:38.822178	110	EXECUTED	9:3f332e13e90739ed0c35b0b25b7822ca	addColumn tableName=CLIENT_ATTRIBUTES; update tableName=CLIENT_ATTRIBUTES; dropColumn columnName=VALUE, tableName=CLIENT_ATTRIBUTES; renameColumn newColumnName=VALUE, oldColumnName=VALUE_NEW, tableName=CLIENT_ATTRIBUTES		\N	4.23.2	\N	\N	7916214219
21.0.2-17277	keycloak	META-INF/jpa-changelog-21.0.2.xml	2025-09-15 06:03:38.82864	111	EXECUTED	9:7ee1f7a3fb8f5588f171fb9a6ab623c0	customChange		\N	4.23.2	\N	\N	7916214219
21.1.0-19404	keycloak	META-INF/jpa-changelog-21.1.0.xml	2025-09-15 06:03:38.888735	112	EXECUTED	9:3d7e830b52f33676b9d64f7f2b2ea634	modifyDataType columnName=DECISION_STRATEGY, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=LOGIC, tableName=RESOURCE_SERVER_POLICY; modifyDataType columnName=POLICY_ENFORCE_MODE, tableName=RESOURCE_SERVER		\N	4.23.2	\N	\N	7916214219
21.1.0-19404-2	keycloak	META-INF/jpa-changelog-21.1.0.xml	2025-09-15 06:03:38.896854	113	MARK_RAN	9:627d032e3ef2c06c0e1f73d2ae25c26c	addColumn tableName=RESOURCE_SERVER_POLICY; update tableName=RESOURCE_SERVER_POLICY; dropColumn columnName=DECISION_STRATEGY, tableName=RESOURCE_SERVER_POLICY; renameColumn newColumnName=DECISION_STRATEGY, oldColumnName=DECISION_STRATEGY_NEW, tabl...		\N	4.23.2	\N	\N	7916214219
22.0.0-17484-updated	keycloak	META-INF/jpa-changelog-22.0.0.xml	2025-09-15 06:03:38.905722	114	EXECUTED	9:90af0bfd30cafc17b9f4d6eccd92b8b3	customChange		\N	4.23.2	\N	\N	7916214219
22.0.5-24031	keycloak	META-INF/jpa-changelog-22.0.0.xml	2025-09-15 06:03:38.909868	115	MARK_RAN	9:a60d2d7b315ec2d3eba9e2f145f9df28	customChange		\N	4.23.2	\N	\N	7916214219
23.0.0-12062	keycloak	META-INF/jpa-changelog-23.0.0.xml	2025-09-15 06:03:38.91984	116	EXECUTED	9:2168fbe728fec46ae9baf15bf80927b8	addColumn tableName=COMPONENT_CONFIG; update tableName=COMPONENT_CONFIG; dropColumn columnName=VALUE, tableName=COMPONENT_CONFIG; renameColumn newColumnName=VALUE, oldColumnName=VALUE_NEW, tableName=COMPONENT_CONFIG		\N	4.23.2	\N	\N	7916214219
23.0.0-17258	keycloak	META-INF/jpa-changelog-23.0.0.xml	2025-09-15 06:03:38.927775	117	EXECUTED	9:36506d679a83bbfda85a27ea1864dca8	addColumn tableName=EVENT_ENTITY		\N	4.23.2	\N	\N	7916214219
\.


--
-- Data for Name: databasechangeloglock; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.databasechangeloglock (id, locked, lockgranted, lockedby) FROM stdin;
1	f	\N	\N
1000	f	\N	\N
1001	f	\N	\N
\.


--
-- Data for Name: default_client_scope; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.default_client_scope (realm_id, scope_id, default_scope) FROM stdin;
14fac002-cb23-4725-a783-9c18edf80bf3	448d81ba-6642-43a9-aaa0-baf38053cc96	f
14fac002-cb23-4725-a783-9c18edf80bf3	d96ac1b2-3d93-4f42-9638-18f1948cb297	t
14fac002-cb23-4725-a783-9c18edf80bf3	e4c684ee-1633-4731-8261-e226a26abcb9	t
14fac002-cb23-4725-a783-9c18edf80bf3	97d27c99-ba37-4c08-8d4e-486fee534821	t
14fac002-cb23-4725-a783-9c18edf80bf3	df8c6c75-840a-452d-b7ce-c500c419fc03	f
14fac002-cb23-4725-a783-9c18edf80bf3	966cabda-1a35-4cb7-a055-bd6d52e7c61d	f
14fac002-cb23-4725-a783-9c18edf80bf3	58948aa3-da26-4aca-88a4-c97f46c33083	t
14fac002-cb23-4725-a783-9c18edf80bf3	6c51377d-5034-413d-8851-d472093f9bdb	t
14fac002-cb23-4725-a783-9c18edf80bf3	0ada5da4-3be3-4c4f-9839-82f104818b4f	f
14fac002-cb23-4725-a783-9c18edf80bf3	bcd933cc-bcb8-4a07-b038-b8e374c4a46f	t
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	79846102-8d69-4b20-b755-d82b12e29141	f
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	5c732fc0-2970-426a-ab23-2c6c18e3a40f	t
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	acea0abc-bad0-463c-852a-e8a010d5b295	t
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	2c3ba811-421d-47a8-9015-ba3ee72d6234	t
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	60ff02d4-9d59-42ea-94ac-133798a98410	f
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	cee67bd3-4b78-4e93-ac4a-1c8da33208a2	f
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	19640b5f-a21f-4c16-9c0c-30c546452054	t
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	5d09c6a8-9864-49bd-8ee3-3161e56d1139	t
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1	f
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa	t
\.


--
-- Data for Name: event_entity; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.event_entity (id, client_id, details_json, error, ip_address, realm_id, session_id, event_time, type, user_id, details_json_long_value) FROM stdin;
3368155d-2989-4301-b997-6261d308fd7a	fanwei	\N	user_not_found	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759048048807	LOGIN_ERROR	\N	{"auth_method":"openid-connect","auth_type":"code","redirect_uri":"http://localhost:3002/sso/auth","code_id":"4c309191-454e-4e3c-89f3-0ca8421d6b8c","username":"admin"}
0fb803af-faa3-4820-aafb-b0a83044dfc9	fanwei	\N	\N	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	4c309191-454e-4e3c-89f3-0ca8421d6b8c	1759048053268	LOGIN	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	{"auth_method":"openid-connect","auth_type":"code","redirect_uri":"http://localhost:3002/sso/auth","consent":"no_consent_required","code_id":"4c309191-454e-4e3c-89f3-0ca8421d6b8c","username":"sso1"}
e450c9d9-fd2e-41f8-bc80-a4fafd50fd11	fanwei	\N	\N	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	4c309191-454e-4e3c-89f3-0ca8421d6b8c	1759048053291	CODE_TO_TOKEN	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	{"token_id":"00ff8c24-04fa-46bb-9a1b-8661aa501d7b","grant_type":"authorization_code","refresh_token_type":"Refresh","scope":"openid profile email","refresh_token_id":"b619f04c-4e79-4a81-9d34-2c742b7ed7f6","code_id":"4c309191-454e-4e3c-89f3-0ca8421d6b8c","client_auth_method":"client-secret"}
6470d789-acbc-463a-b6f4-9040b0a9a0a5	fanwei	\N	invalid_redirect_uri	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759048524640	LOGIN_ERROR	\N	{"redirect_uri":"http://localhost:3003/sso/auth"}
af99c3d4-7460-441a-9e08-d66acb7e44f0	fanwei	\N	invalid_redirect_uri	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759048529986	LOGIN_ERROR	\N	{"redirect_uri":"http://localhost:3003/sso/auth"}
e7eccccd-9817-4cfd-8a61-da0536b064d4	fanwei	\N	invalid_redirect_uri	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759048644445	LOGIN_ERROR	\N	{"redirect_uri":"http://localhost:3003/sso/auth"}
a1453c1b-3b86-4e50-9657-0e2eff755b8e	fanwei	\N	invalid_redirect_uri	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759048648493	LOGIN_ERROR	\N	{"redirect_uri":"http://localhost:3003/sso/auth"}
278f4d5d-787a-473c-80fd-6dd813586100	fanwei	\N	invalid_redirect_uri	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759048649242	LOGIN_ERROR	\N	{"redirect_uri":"http://localhost:3003/sso/auth"}
2eaec1e9-3a46-4b2b-9f9c-735640ef2624	fanwei	\N	\N	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	4c309191-454e-4e3c-89f3-0ca8421d6b8c	1759048669354	LOGIN	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	{"auth_method":"openid-connect","auth_type":"code","response_type":"code","redirect_uri":"http://localhost:3002/sso/auth","consent":"no_consent_required","code_id":"4c309191-454e-4e3c-89f3-0ca8421d6b8c","response_mode":"query","username":"sso1"}
406edb63-4db6-4ac1-8b61-f42ff6b224ca	fanwei	\N	\N	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	4c309191-454e-4e3c-89f3-0ca8421d6b8c	1759048669376	CODE_TO_TOKEN	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	{"token_id":"c56492e8-ac8a-4e55-942d-d725192166c1","grant_type":"authorization_code","refresh_token_type":"Refresh","scope":"openid profile email","refresh_token_id":"0189e7e3-1923-4eda-ba08-a7761224c60f","code_id":"4c309191-454e-4e3c-89f3-0ca8421d6b8c","client_auth_method":"client-secret"}
072a1e4c-0c33-4dee-8f14-b6f0776688f5	fanwei	\N	\N	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	4c309191-454e-4e3c-89f3-0ca8421d6b8c	1759048677730	LOGIN	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	{"auth_method":"openid-connect","auth_type":"code","response_type":"code","redirect_uri":"http://localhost:3002/sso/auth","consent":"no_consent_required","code_id":"144f4cca-65ea-4bda-9e96-9d0f98c7b7d5","response_mode":"query","username":"sso1"}
bcc366e9-ee0c-477d-a01b-d52bb0be3288	fanwei	\N	\N	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	4c309191-454e-4e3c-89f3-0ca8421d6b8c	1759048677786	CODE_TO_TOKEN	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	{"token_id":"44a42f08-634f-4031-8ead-207b329c6e3a","grant_type":"authorization_code","refresh_token_type":"Refresh","scope":"openid profile email","refresh_token_id":"e3001d6d-95f3-49ad-a735-df961c7eead6","code_id":"4c309191-454e-4e3c-89f3-0ca8421d6b8c","client_auth_method":"client-secret"}
29a9aff5-44a9-418b-813d-6fea54469a03	fanwei	\N	\N	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	4c309191-454e-4e3c-89f3-0ca8421d6b8c	1759048686599	LOGOUT	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	{"redirect_uri":"http://localhost:3002"}
8848be25-c5f8-4281-89d3-a675666d6f9c	tingche	\N	invalid_redirect_uri	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759049764786	LOGIN_ERROR	\N	{"redirect_uri":"http://localhost:3002/sso/auth"}
b8cbf0ee-b906-4ad6-8e8c-62135f213e2d	tingche	\N	\N	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	02fa0b2d-fbf1-4473-81c3-5025c85d4082	1759050916418	LOGIN	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	{"auth_method":"openid-connect","auth_type":"code","redirect_uri":"http://localhost:3001/sso/auth","consent":"no_consent_required","code_id":"02fa0b2d-fbf1-4473-81c3-5025c85d4082","username":"sso1"}
d9b160da-aa47-45c5-81b6-9364aa3e3227	tingche	\N	\N	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	02fa0b2d-fbf1-4473-81c3-5025c85d4082	1759050916474	CODE_TO_TOKEN	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	{"token_id":"08ac6f26-539a-42ee-9760-96262960091e","grant_type":"authorization_code","refresh_token_type":"Refresh","scope":"openid profile email","refresh_token_id":"5d910a62-cc70-4a04-a85a-2fc720f4b3e7","code_id":"02fa0b2d-fbf1-4473-81c3-5025c85d4082","client_auth_method":"client-secret"}
b708b5f1-98f7-408e-8b17-b2c6dfb77ad2	tingche	\N	\N	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	02fa0b2d-fbf1-4473-81c3-5025c85d4082	1759051460090	LOGIN	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	{"auth_method":"openid-connect","auth_type":"code","response_type":"code","redirect_uri":"http://localhost:3001/sso/auth","consent":"no_consent_required","code_id":"02fa0b2d-fbf1-4473-81c3-5025c85d4082","response_mode":"query","username":"sso1"}
f00d77fe-057a-41a7-9ae6-c47d47b5c6a6	tingche	\N	\N	172.20.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	02fa0b2d-fbf1-4473-81c3-5025c85d4082	1759051460190	CODE_TO_TOKEN	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	{"token_id":"cb66b113-06d6-47e2-a87f-34b53208213c","grant_type":"authorization_code","refresh_token_type":"Refresh","scope":"openid profile email","refresh_token_id":"3f16601d-4666-421e-a9a9-8284ca9f6fdb","code_id":"02fa0b2d-fbf1-4473-81c3-5025c85d4082","client_auth_method":"client-secret"}
52c4f5d0-a2c4-4754-a9fe-1f610b4a192b	\N	\N	invalid_request	172.19.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759222197688	LOGIN_ERROR	\N	null
5e2c9f07-ac4a-4d49-b175-0203b88ef1ff	\N	\N	invalid_request	172.19.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759222199161	LOGIN_ERROR	\N	null
bd23e381-44b7-492e-b6f7-822a2f46c7bb	\N	\N	invalid_request	172.19.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759222199939	LOGIN_ERROR	\N	null
5dc96445-669f-49f4-826f-0c3c6be37fbb	\N	\N	invalid_request	172.19.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759222200124	LOGIN_ERROR	\N	null
01685603-4aa0-4f42-ac6b-a08a1533f6ab	\N	\N	invalid_request	172.19.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759222200284	LOGIN_ERROR	\N	null
8b92c252-57b8-4977-83bc-0abd5ddf112a	\N	\N	invalid_request	172.19.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759222930037	LOGIN_ERROR	\N	null
86294fab-6556-41af-b050-0a0c6315e44a	\N	\N	invalid_request	172.19.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759222931351	LOGIN_ERROR	\N	null
7746fdce-8a3e-46fb-93e5-2e3ffb9cffbc	\N	\N	invalid_request	172.19.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759222931662	LOGIN_ERROR	\N	null
c5e5de90-4530-4f0f-b69a-b05e838c6fbe	\N	\N	invalid_request	172.19.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759222931845	LOGIN_ERROR	\N	null
881d7a90-3e5a-4300-aed4-051217d69ac2	\N	\N	invalid_request	172.19.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759222932029	LOGIN_ERROR	\N	null
c36cdff2-9ff0-45b9-b483-95fa317b790b	\N	\N	invalid_request	172.19.0.1	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	1759222932210	LOGIN_ERROR	\N	null
\.


--
-- Data for Name: fed_user_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.fed_user_attribute (id, name, user_id, realm_id, storage_provider_id, value) FROM stdin;
\.


--
-- Data for Name: fed_user_consent; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.fed_user_consent (id, client_id, user_id, realm_id, storage_provider_id, created_date, last_updated_date, client_storage_provider, external_client_id) FROM stdin;
\.


--
-- Data for Name: fed_user_consent_cl_scope; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.fed_user_consent_cl_scope (user_consent_id, scope_id) FROM stdin;
\.


--
-- Data for Name: fed_user_credential; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.fed_user_credential (id, salt, type, created_date, user_id, realm_id, storage_provider_id, user_label, secret_data, credential_data, priority) FROM stdin;
\.


--
-- Data for Name: fed_user_group_membership; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.fed_user_group_membership (group_id, user_id, realm_id, storage_provider_id) FROM stdin;
\.


--
-- Data for Name: fed_user_required_action; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.fed_user_required_action (required_action, user_id, realm_id, storage_provider_id) FROM stdin;
\.


--
-- Data for Name: fed_user_role_mapping; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.fed_user_role_mapping (role_id, user_id, realm_id, storage_provider_id) FROM stdin;
\.


--
-- Data for Name: federated_identity; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.federated_identity (identity_provider, realm_id, federated_user_id, federated_username, token, user_id) FROM stdin;
\.


--
-- Data for Name: federated_user; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.federated_user (id, storage_provider_id, realm_id) FROM stdin;
\.


--
-- Data for Name: group_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.group_attribute (id, name, value, group_id) FROM stdin;
\.


--
-- Data for Name: group_role_mapping; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.group_role_mapping (role_id, group_id) FROM stdin;
\.


--
-- Data for Name: identity_provider; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.identity_provider (internal_id, enabled, provider_alias, provider_id, store_token, authenticate_by_default, realm_id, add_token_role, trust_email, first_broker_login_flow_id, post_broker_login_flow_id, provider_display_name, link_only) FROM stdin;
\.


--
-- Data for Name: identity_provider_config; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.identity_provider_config (identity_provider_id, value, name) FROM stdin;
\.


--
-- Data for Name: identity_provider_mapper; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.identity_provider_mapper (id, name, idp_alias, idp_mapper_name, realm_id) FROM stdin;
\.


--
-- Data for Name: idp_mapper_config; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.idp_mapper_config (idp_mapper_id, value, name) FROM stdin;
\.


--
-- Data for Name: keycloak_group; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.keycloak_group (id, name, parent_group, realm_id) FROM stdin;
\.


--
-- Data for Name: keycloak_role; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.keycloak_role (id, client_realm_constraint, client_role, description, name, realm_id, client, realm) FROM stdin;
aa39db72-82f8-431a-abc5-a5c0b1ff9db6	14fac002-cb23-4725-a783-9c18edf80bf3	f	${role_default-roles}	default-roles-master	14fac002-cb23-4725-a783-9c18edf80bf3	\N	\N
85fd5a68-0929-4cf6-a975-a479734e3b5f	14fac002-cb23-4725-a783-9c18edf80bf3	f	${role_admin}	admin	14fac002-cb23-4725-a783-9c18edf80bf3	\N	\N
b1cdaef5-05d8-4643-8c9a-a1f88dbcce38	14fac002-cb23-4725-a783-9c18edf80bf3	f	${role_create-realm}	create-realm	14fac002-cb23-4725-a783-9c18edf80bf3	\N	\N
abd96358-0cb5-4850-945e-1ac027d1f9cd	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_create-client}	create-client	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
7bcb5981-e18c-466e-8f4a-7043f9a175b1	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_view-realm}	view-realm	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
ba86fde9-5de1-4ff2-9963-1e3ead009381	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_view-users}	view-users	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
6752adef-c89e-4041-918f-4a6f4b852333	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_view-clients}	view-clients	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
54325cd3-6f42-4ae1-adf4-365395213ea1	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_view-events}	view-events	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
b629142a-cc37-4003-8db4-467ade17f444	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_view-identity-providers}	view-identity-providers	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
6e6c5210-d873-4872-8c81-cf1978d17e1c	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_view-authorization}	view-authorization	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
03d76b29-ad86-4a40-9e96-d15733041561	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_manage-realm}	manage-realm	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
fe80fd64-5e01-4bdd-af8e-b2da214c55cf	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_manage-users}	manage-users	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
cebb17b7-4ebf-4fba-bc5e-b8c7bc33806b	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_manage-clients}	manage-clients	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
60d8d8e0-1b7f-45cd-86a9-f273292e16a8	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_manage-events}	manage-events	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
c8867a69-42e3-4c5b-8721-98b83427f16f	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_manage-identity-providers}	manage-identity-providers	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
ab975b35-4d6c-4bbd-a1fd-f27ba06f7506	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_manage-authorization}	manage-authorization	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
795de1be-8d9c-4d43-970a-b222ef9ec7dd	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_query-users}	query-users	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
2cdd4831-7685-40e4-83b5-bc694384a328	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_query-clients}	query-clients	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
b2a66d6c-7f14-4344-a096-b6ee3d6ec6ee	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_query-realms}	query-realms	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
9d4c8746-5a68-4b27-9308-d170d96ce9fa	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_query-groups}	query-groups	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
57e60180-30c9-42df-9be3-245c5f73be19	6959959b-71a5-48a6-862a-cee83b060340	t	${role_view-profile}	view-profile	14fac002-cb23-4725-a783-9c18edf80bf3	6959959b-71a5-48a6-862a-cee83b060340	\N
b8d13ee3-8a59-4da6-a545-abb269a7f7c7	6959959b-71a5-48a6-862a-cee83b060340	t	${role_manage-account}	manage-account	14fac002-cb23-4725-a783-9c18edf80bf3	6959959b-71a5-48a6-862a-cee83b060340	\N
ff417934-a308-4c72-85db-f3e5ae0c8191	6959959b-71a5-48a6-862a-cee83b060340	t	${role_manage-account-links}	manage-account-links	14fac002-cb23-4725-a783-9c18edf80bf3	6959959b-71a5-48a6-862a-cee83b060340	\N
95b9dc44-43ab-43d6-9cad-9ffe32f22562	6959959b-71a5-48a6-862a-cee83b060340	t	${role_view-applications}	view-applications	14fac002-cb23-4725-a783-9c18edf80bf3	6959959b-71a5-48a6-862a-cee83b060340	\N
8909c9b6-b2ef-4666-b717-ca3170ddce5c	6959959b-71a5-48a6-862a-cee83b060340	t	${role_view-consent}	view-consent	14fac002-cb23-4725-a783-9c18edf80bf3	6959959b-71a5-48a6-862a-cee83b060340	\N
11293df3-fdc8-4f14-afb5-b414b1b92c8d	6959959b-71a5-48a6-862a-cee83b060340	t	${role_manage-consent}	manage-consent	14fac002-cb23-4725-a783-9c18edf80bf3	6959959b-71a5-48a6-862a-cee83b060340	\N
2b3f3938-e2ed-4676-be30-58652949f356	6959959b-71a5-48a6-862a-cee83b060340	t	${role_view-groups}	view-groups	14fac002-cb23-4725-a783-9c18edf80bf3	6959959b-71a5-48a6-862a-cee83b060340	\N
91848ad2-098c-436f-b842-6a7e9b9a9636	6959959b-71a5-48a6-862a-cee83b060340	t	${role_delete-account}	delete-account	14fac002-cb23-4725-a783-9c18edf80bf3	6959959b-71a5-48a6-862a-cee83b060340	\N
e5d4af34-3cd3-4048-859a-693a78015df4	5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	t	${role_read-token}	read-token	14fac002-cb23-4725-a783-9c18edf80bf3	5d3fd394-bdbb-4a37-ba7f-f078e6c30b6f	\N
fc4dc982-3d29-4f7a-8821-9340671a9d96	ac0e21be-bc92-4767-8f0a-4ac31622d64e	t	${role_impersonation}	impersonation	14fac002-cb23-4725-a783-9c18edf80bf3	ac0e21be-bc92-4767-8f0a-4ac31622d64e	\N
48f095eb-56dc-4d2f-ba8c-1376fb23a7ae	14fac002-cb23-4725-a783-9c18edf80bf3	f	${role_offline-access}	offline_access	14fac002-cb23-4725-a783-9c18edf80bf3	\N	\N
05978310-6e55-4152-8508-7ddede830f44	14fac002-cb23-4725-a783-9c18edf80bf3	f	${role_uma_authorization}	uma_authorization	14fac002-cb23-4725-a783-9c18edf80bf3	\N	\N
9b8fca5f-61de-4a42-8acf-ca96ac82022c	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	f	${role_default-roles}	default-roles-ps-realm	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	\N
bc9ace77-0a56-4bd9-aea0-ab14b7f89d5b	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_create-client}	create-client	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
41e8ec6a-11e4-4f52-a2b1-662987a41f0c	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_view-realm}	view-realm	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
8bf8c5ec-ef13-4fe5-a052-4108ca30336e	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_view-users}	view-users	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
133a57c3-9e4f-4418-8548-e89ba80c331d	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_view-clients}	view-clients	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
534d3622-980d-4329-b17e-b53ad3428014	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_view-events}	view-events	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
678bc0e6-054a-42f7-ad7d-9ea4a201bdcc	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_view-identity-providers}	view-identity-providers	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
53237d96-fbf0-44d4-aa94-a1cd03e93ab6	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_view-authorization}	view-authorization	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
4eeafba1-202d-450a-a6fc-c2e9fbcd407e	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_manage-realm}	manage-realm	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
b6012373-27ff-4bd6-8b5c-86c36804a2ee	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_manage-users}	manage-users	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
68b257a6-fe4c-4661-bc2d-0249c445473f	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_manage-clients}	manage-clients	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
004a94a4-545c-42a0-a2b2-6d127b1d6eab	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_manage-events}	manage-events	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
0ce56b57-b985-43d8-94a7-c5a9ac2eb3c2	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_manage-identity-providers}	manage-identity-providers	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
8406c6b1-beb7-4e5b-a11c-9c5c1af86363	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_manage-authorization}	manage-authorization	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
74278abc-772d-44fd-85c0-fe4c3ca01d01	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_query-users}	query-users	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
505230c6-972f-45e3-927a-6c2bc9f1d36b	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_query-clients}	query-clients	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
1cf85f9f-b8e3-4d46-97c6-31b6fa944d24	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_query-realms}	query-realms	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
0850b683-de4a-4374-bd30-325136f64fdc	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_query-groups}	query-groups	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
4a1fd3ef-2e4e-4ae4-a517-175c0675762a	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_realm-admin}	realm-admin	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
72b2ca13-d5c8-4dc5-8e68-30a2eb321ac0	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_create-client}	create-client	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
d88c6b54-433b-48cb-8611-80a3a4b42215	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_view-realm}	view-realm	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
4b04f205-d99c-4fd1-87e3-916aa8b5ae02	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_view-users}	view-users	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
8028588a-2c42-42da-82f6-b2c1b41c0b8d	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_view-clients}	view-clients	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
828d9326-474f-432d-9764-ecaf096936b4	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_view-events}	view-events	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
cd6c9708-ca88-41d1-a02d-f2b7579ebfa4	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_view-identity-providers}	view-identity-providers	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
e79300d5-02fd-4e89-8d8b-a66cc178f0f7	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_view-authorization}	view-authorization	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
68baf511-fed0-4b64-ae33-900bd07c6b16	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_manage-realm}	manage-realm	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
6019f312-99af-4b5c-8c6f-b235c6920e99	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_manage-users}	manage-users	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
ae46661f-f292-4bbb-acf3-e796491ab5a3	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_manage-clients}	manage-clients	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
1d897f99-be30-4532-8e2c-7bf06ab079a5	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_manage-events}	manage-events	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
0aff7ef3-8384-49ca-aa70-8a2d02dd1690	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_manage-identity-providers}	manage-identity-providers	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
7520abf1-8b78-454d-9232-fe737c673433	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_manage-authorization}	manage-authorization	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
a27af109-b3d4-4af0-8de1-8397a5cdfda8	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_query-users}	query-users	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
656d53df-6337-4315-8e11-99bc2c35851c	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_query-clients}	query-clients	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
336a7990-c7b4-4bb6-a375-9202d321411f	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_query-realms}	query-realms	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
54e7ac49-274e-4eb5-b8cf-65924cedf62c	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_query-groups}	query-groups	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
e83bf8a4-8032-4ab4-9bd5-374146ada0d9	d935986d-48a9-4dfd-a6ef-215644092a3f	t	${role_view-profile}	view-profile	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	d935986d-48a9-4dfd-a6ef-215644092a3f	\N
c12f1d5f-7b61-43d8-abe9-a2d855c196a4	d935986d-48a9-4dfd-a6ef-215644092a3f	t	${role_manage-account}	manage-account	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	d935986d-48a9-4dfd-a6ef-215644092a3f	\N
88b1bef9-d980-4cde-bd59-5f89b4056f60	d935986d-48a9-4dfd-a6ef-215644092a3f	t	${role_manage-account-links}	manage-account-links	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	d935986d-48a9-4dfd-a6ef-215644092a3f	\N
f086b022-f928-4a1d-93df-6de803892c45	d935986d-48a9-4dfd-a6ef-215644092a3f	t	${role_view-applications}	view-applications	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	d935986d-48a9-4dfd-a6ef-215644092a3f	\N
46f2b851-6beb-4900-a772-78bc497f6fec	d935986d-48a9-4dfd-a6ef-215644092a3f	t	${role_view-consent}	view-consent	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	d935986d-48a9-4dfd-a6ef-215644092a3f	\N
c1048fbf-8c9a-45c7-ade7-01360a368238	d935986d-48a9-4dfd-a6ef-215644092a3f	t	${role_manage-consent}	manage-consent	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	d935986d-48a9-4dfd-a6ef-215644092a3f	\N
4adb08d1-3a97-436e-8865-c1245701906c	d935986d-48a9-4dfd-a6ef-215644092a3f	t	${role_view-groups}	view-groups	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	d935986d-48a9-4dfd-a6ef-215644092a3f	\N
6fcedafc-0c87-4f30-b40b-28c8f4a408e2	d935986d-48a9-4dfd-a6ef-215644092a3f	t	${role_delete-account}	delete-account	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	d935986d-48a9-4dfd-a6ef-215644092a3f	\N
3b718ee8-b4a5-4f32-a5e8-702b40398bb1	d72824fb-285d-4a76-ac38-cd55d99064aa	t	${role_impersonation}	impersonation	14fac002-cb23-4725-a783-9c18edf80bf3	d72824fb-285d-4a76-ac38-cd55d99064aa	\N
d52efe31-8ee1-4599-88de-661968ece9b7	740e44d0-d37d-4bf6-a17e-31556b54fa34	t	${role_impersonation}	impersonation	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	740e44d0-d37d-4bf6-a17e-31556b54fa34	\N
066b0689-0d38-4c19-bc45-624af2113312	05465146-c8ef-43c7-b824-29857a1e181e	t	${role_read-token}	read-token	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	05465146-c8ef-43c7-b824-29857a1e181e	\N
cdeaf4d6-ddf9-4be6-9f4b-d6483659896b	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	f	${role_offline-access}	offline_access	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	\N
1a04f8c8-3692-49bb-87c9-cddd1708c80a	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	f	${role_uma_authorization}	uma_authorization	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	\N	\N
\.


--
-- Data for Name: migration_model; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.migration_model (id, version, update_time) FROM stdin;
b77os	23.0.1	1757916219
\.


--
-- Data for Name: offline_client_session; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.offline_client_session (user_session_id, client_id, offline_flag, "timestamp", data, client_storage_provider, external_client_id) FROM stdin;
\.


--
-- Data for Name: offline_user_session; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.offline_user_session (user_session_id, user_id, realm_id, created_on, offline_flag, data, last_session_refresh) FROM stdin;
\.


--
-- Data for Name: policy_config; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.policy_config (policy_id, name, value) FROM stdin;
\.


--
-- Data for Name: protocol_mapper; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.protocol_mapper (id, name, protocol, protocol_mapper_name, client_id, client_scope_id) FROM stdin;
0cb9efb0-3c92-4773-8fe5-03ff18636d7d	audience resolve	openid-connect	oidc-audience-resolve-mapper	f9934ba3-18fc-459e-a566-40290c557df0	\N
38dd8f2b-0f3a-4274-86b7-24a39dae616a	locale	openid-connect	oidc-usermodel-attribute-mapper	decb93ba-5e1b-4e0d-b297-1b3f69532f47	\N
ae2301f6-44ae-42a5-a4f3-01b01c825076	role list	saml	saml-role-list-mapper	\N	d96ac1b2-3d93-4f42-9638-18f1948cb297
9776f5a7-3e3e-4f57-8390-c8dd0c0403ac	full name	openid-connect	oidc-full-name-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
0724ffe9-f74a-42e7-9182-15b3ae7ce6ce	family name	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
a21859a1-290c-4e09-85ba-627ef466263b	given name	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
c29b327b-451c-485b-add8-d6ebbfe6ec9a	middle name	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
55393550-c27f-40cd-89d6-f64c0ce06393	nickname	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
059dc41e-1085-49ea-9471-a2e58933d1fb	username	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
f4cb4678-2048-4e86-ac9f-2299433f7ff5	profile	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
15d417f1-37ec-4bdd-aa07-be7a4000ac5f	picture	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
4436e755-86b0-498d-819b-000ab190d8f1	website	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
46aab1a0-3e77-475b-888a-ba837dedf72e	gender	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
a8d8f804-2952-4084-85ba-66454b398738	birthdate	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
ec993500-78ce-413d-b95e-ef4e11aa724b	zoneinfo	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
256a2d01-3dc3-4fa0-b9b0-2183b99a0316	locale	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
a4cc7d08-c313-40ae-a236-0a12653b2c87	updated at	openid-connect	oidc-usermodel-attribute-mapper	\N	e4c684ee-1633-4731-8261-e226a26abcb9
66a3b444-aa3d-4a4b-a0b7-27e3a34536bd	email	openid-connect	oidc-usermodel-attribute-mapper	\N	97d27c99-ba37-4c08-8d4e-486fee534821
ebb03946-008b-443e-aff6-c6310a5e384a	email verified	openid-connect	oidc-usermodel-property-mapper	\N	97d27c99-ba37-4c08-8d4e-486fee534821
6136d4fa-de96-40ea-bd5c-af7dcf8760db	address	openid-connect	oidc-address-mapper	\N	df8c6c75-840a-452d-b7ce-c500c419fc03
45943ed8-80ed-4d94-816a-8653e1131b84	phone number	openid-connect	oidc-usermodel-attribute-mapper	\N	966cabda-1a35-4cb7-a055-bd6d52e7c61d
194fdf44-787e-4c5c-8c1a-74db8c2c5f31	phone number verified	openid-connect	oidc-usermodel-attribute-mapper	\N	966cabda-1a35-4cb7-a055-bd6d52e7c61d
737092d4-1e96-474a-bd17-e783678bde69	realm roles	openid-connect	oidc-usermodel-realm-role-mapper	\N	58948aa3-da26-4aca-88a4-c97f46c33083
ccb7b4a7-5b7c-44e1-9fd5-4444578e869a	client roles	openid-connect	oidc-usermodel-client-role-mapper	\N	58948aa3-da26-4aca-88a4-c97f46c33083
d6c29ad6-9ac5-46b1-86b2-9dca3f7f83b7	audience resolve	openid-connect	oidc-audience-resolve-mapper	\N	58948aa3-da26-4aca-88a4-c97f46c33083
1e01a8f0-79dc-4664-a79c-76f7f8bf1a61	allowed web origins	openid-connect	oidc-allowed-origins-mapper	\N	6c51377d-5034-413d-8851-d472093f9bdb
c53ba66c-1754-4c2d-881a-1911c9ef4768	upn	openid-connect	oidc-usermodel-attribute-mapper	\N	0ada5da4-3be3-4c4f-9839-82f104818b4f
c1f28afd-8d51-4c4f-afad-c6895fbdcdb3	groups	openid-connect	oidc-usermodel-realm-role-mapper	\N	0ada5da4-3be3-4c4f-9839-82f104818b4f
977cad4a-9301-4e4a-9bb9-d2e499348346	acr loa level	openid-connect	oidc-acr-mapper	\N	bcd933cc-bcb8-4a07-b038-b8e374c4a46f
462acec3-f8c4-4d76-8c70-87ca338cf242	audience resolve	openid-connect	oidc-audience-resolve-mapper	136f4d7c-de2f-40e0-ba23-c3183b70c007	\N
47dacb3d-2e4c-45b3-a003-923e62c89912	role list	saml	saml-role-list-mapper	\N	5c732fc0-2970-426a-ab23-2c6c18e3a40f
f76dbe8a-d934-44bc-bc55-eb49eba23c21	full name	openid-connect	oidc-full-name-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
d3851ff0-356b-45ab-83d2-79f8f5d46600	family name	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
947568d2-b970-4773-b554-bfe3d5d90ff8	given name	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
cd7a9873-c5c4-430a-b012-d689ba802a33	middle name	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
2e08099f-fc9a-48b4-9f10-70a2fe9c05d7	nickname	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
c2a91f20-e55d-46fb-bb12-a8b536f6479e	username	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
27ecfbdc-a4d0-4074-a8a6-704b4e273b60	profile	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
c59e006d-128b-4660-b5ac-666dcc8439d1	picture	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
094833ae-1ffe-4c26-a5f4-d8d3a79f4eca	website	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
152b94d9-2724-435d-a075-46c164d60ff1	gender	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
232a3ff9-b9c3-446f-82cc-08726feb088a	birthdate	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
0c66c3fd-5eb8-481e-81a2-a41568394010	zoneinfo	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
2838016e-3734-4f70-bce1-00661d942465	locale	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
6342846a-d077-4c3b-a5db-34afc567196b	updated at	openid-connect	oidc-usermodel-attribute-mapper	\N	acea0abc-bad0-463c-852a-e8a010d5b295
6a61553b-70cf-4202-812a-fbbd7b9396da	email	openid-connect	oidc-usermodel-attribute-mapper	\N	2c3ba811-421d-47a8-9015-ba3ee72d6234
13bec266-944d-4cc9-8844-4b07f5715214	email verified	openid-connect	oidc-usermodel-property-mapper	\N	2c3ba811-421d-47a8-9015-ba3ee72d6234
066b9b72-2e25-4c91-82d8-823baed69972	address	openid-connect	oidc-address-mapper	\N	60ff02d4-9d59-42ea-94ac-133798a98410
63aa39b8-cea9-4bd4-9a4b-d4e1b6939623	phone number	openid-connect	oidc-usermodel-attribute-mapper	\N	cee67bd3-4b78-4e93-ac4a-1c8da33208a2
531563de-c62f-4836-83e2-c5c3644e45a8	phone number verified	openid-connect	oidc-usermodel-attribute-mapper	\N	cee67bd3-4b78-4e93-ac4a-1c8da33208a2
1bfbcf35-eb71-4b15-997d-f0fe341604f3	realm roles	openid-connect	oidc-usermodel-realm-role-mapper	\N	19640b5f-a21f-4c16-9c0c-30c546452054
f3fdb43d-afce-4112-ab96-9e703d5b21ef	client roles	openid-connect	oidc-usermodel-client-role-mapper	\N	19640b5f-a21f-4c16-9c0c-30c546452054
610d404f-31bc-4280-a728-487f474f7291	audience resolve	openid-connect	oidc-audience-resolve-mapper	\N	19640b5f-a21f-4c16-9c0c-30c546452054
e7b1ba62-b7a2-40c3-84da-6df86948daf1	allowed web origins	openid-connect	oidc-allowed-origins-mapper	\N	5d09c6a8-9864-49bd-8ee3-3161e56d1139
bdc766a9-f141-4b2e-a1f8-c798b29dff00	upn	openid-connect	oidc-usermodel-attribute-mapper	\N	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1
adbaade4-e24f-4671-a3a9-6ae432fbe9ad	groups	openid-connect	oidc-usermodel-realm-role-mapper	\N	9b48e9e8-6ff1-408c-9bfd-42bc1b821ce1
a4629fb2-14c2-4a21-a66f-613738e4ff7d	acr loa level	openid-connect	oidc-acr-mapper	\N	4f1cc7e5-ccb0-4f05-a59d-c0b1b67601aa
888a7e60-800a-4022-abd3-695394e206b2	locale	openid-connect	oidc-usermodel-attribute-mapper	9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	\N
d50928d2-f431-4936-a583-f5c23275c71d	Client ID	openid-connect	oidc-usersessionmodel-note-mapper	0f933f03-43e3-4b50-8a06-ecec138f9dcd	\N
fdf8062f-ce2c-48df-8e62-cf622b1a6600	Client Host	openid-connect	oidc-usersessionmodel-note-mapper	0f933f03-43e3-4b50-8a06-ecec138f9dcd	\N
7c84b23c-2fee-45c7-9f03-e1956a2d43c8	Client IP Address	openid-connect	oidc-usersessionmodel-note-mapper	0f933f03-43e3-4b50-8a06-ecec138f9dcd	\N
\.


--
-- Data for Name: protocol_mapper_config; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.protocol_mapper_config (protocol_mapper_id, value, name) FROM stdin;
38dd8f2b-0f3a-4274-86b7-24a39dae616a	true	introspection.token.claim
38dd8f2b-0f3a-4274-86b7-24a39dae616a	true	userinfo.token.claim
38dd8f2b-0f3a-4274-86b7-24a39dae616a	locale	user.attribute
38dd8f2b-0f3a-4274-86b7-24a39dae616a	true	id.token.claim
38dd8f2b-0f3a-4274-86b7-24a39dae616a	true	access.token.claim
38dd8f2b-0f3a-4274-86b7-24a39dae616a	locale	claim.name
38dd8f2b-0f3a-4274-86b7-24a39dae616a	String	jsonType.label
ae2301f6-44ae-42a5-a4f3-01b01c825076	false	single
ae2301f6-44ae-42a5-a4f3-01b01c825076	Basic	attribute.nameformat
ae2301f6-44ae-42a5-a4f3-01b01c825076	Role	attribute.name
059dc41e-1085-49ea-9471-a2e58933d1fb	true	introspection.token.claim
059dc41e-1085-49ea-9471-a2e58933d1fb	true	userinfo.token.claim
059dc41e-1085-49ea-9471-a2e58933d1fb	username	user.attribute
059dc41e-1085-49ea-9471-a2e58933d1fb	true	id.token.claim
059dc41e-1085-49ea-9471-a2e58933d1fb	true	access.token.claim
059dc41e-1085-49ea-9471-a2e58933d1fb	preferred_username	claim.name
059dc41e-1085-49ea-9471-a2e58933d1fb	String	jsonType.label
0724ffe9-f74a-42e7-9182-15b3ae7ce6ce	true	introspection.token.claim
0724ffe9-f74a-42e7-9182-15b3ae7ce6ce	true	userinfo.token.claim
0724ffe9-f74a-42e7-9182-15b3ae7ce6ce	lastName	user.attribute
0724ffe9-f74a-42e7-9182-15b3ae7ce6ce	true	id.token.claim
0724ffe9-f74a-42e7-9182-15b3ae7ce6ce	true	access.token.claim
0724ffe9-f74a-42e7-9182-15b3ae7ce6ce	family_name	claim.name
0724ffe9-f74a-42e7-9182-15b3ae7ce6ce	String	jsonType.label
15d417f1-37ec-4bdd-aa07-be7a4000ac5f	true	introspection.token.claim
15d417f1-37ec-4bdd-aa07-be7a4000ac5f	true	userinfo.token.claim
15d417f1-37ec-4bdd-aa07-be7a4000ac5f	picture	user.attribute
15d417f1-37ec-4bdd-aa07-be7a4000ac5f	true	id.token.claim
15d417f1-37ec-4bdd-aa07-be7a4000ac5f	true	access.token.claim
15d417f1-37ec-4bdd-aa07-be7a4000ac5f	picture	claim.name
15d417f1-37ec-4bdd-aa07-be7a4000ac5f	String	jsonType.label
256a2d01-3dc3-4fa0-b9b0-2183b99a0316	true	introspection.token.claim
256a2d01-3dc3-4fa0-b9b0-2183b99a0316	true	userinfo.token.claim
256a2d01-3dc3-4fa0-b9b0-2183b99a0316	locale	user.attribute
256a2d01-3dc3-4fa0-b9b0-2183b99a0316	true	id.token.claim
256a2d01-3dc3-4fa0-b9b0-2183b99a0316	true	access.token.claim
256a2d01-3dc3-4fa0-b9b0-2183b99a0316	locale	claim.name
256a2d01-3dc3-4fa0-b9b0-2183b99a0316	String	jsonType.label
4436e755-86b0-498d-819b-000ab190d8f1	true	introspection.token.claim
4436e755-86b0-498d-819b-000ab190d8f1	true	userinfo.token.claim
4436e755-86b0-498d-819b-000ab190d8f1	website	user.attribute
4436e755-86b0-498d-819b-000ab190d8f1	true	id.token.claim
4436e755-86b0-498d-819b-000ab190d8f1	true	access.token.claim
4436e755-86b0-498d-819b-000ab190d8f1	website	claim.name
4436e755-86b0-498d-819b-000ab190d8f1	String	jsonType.label
46aab1a0-3e77-475b-888a-ba837dedf72e	true	introspection.token.claim
46aab1a0-3e77-475b-888a-ba837dedf72e	true	userinfo.token.claim
46aab1a0-3e77-475b-888a-ba837dedf72e	gender	user.attribute
46aab1a0-3e77-475b-888a-ba837dedf72e	true	id.token.claim
46aab1a0-3e77-475b-888a-ba837dedf72e	true	access.token.claim
46aab1a0-3e77-475b-888a-ba837dedf72e	gender	claim.name
46aab1a0-3e77-475b-888a-ba837dedf72e	String	jsonType.label
55393550-c27f-40cd-89d6-f64c0ce06393	true	introspection.token.claim
55393550-c27f-40cd-89d6-f64c0ce06393	true	userinfo.token.claim
55393550-c27f-40cd-89d6-f64c0ce06393	nickname	user.attribute
55393550-c27f-40cd-89d6-f64c0ce06393	true	id.token.claim
55393550-c27f-40cd-89d6-f64c0ce06393	true	access.token.claim
55393550-c27f-40cd-89d6-f64c0ce06393	nickname	claim.name
55393550-c27f-40cd-89d6-f64c0ce06393	String	jsonType.label
9776f5a7-3e3e-4f57-8390-c8dd0c0403ac	true	introspection.token.claim
9776f5a7-3e3e-4f57-8390-c8dd0c0403ac	true	userinfo.token.claim
9776f5a7-3e3e-4f57-8390-c8dd0c0403ac	true	id.token.claim
9776f5a7-3e3e-4f57-8390-c8dd0c0403ac	true	access.token.claim
a21859a1-290c-4e09-85ba-627ef466263b	true	introspection.token.claim
a21859a1-290c-4e09-85ba-627ef466263b	true	userinfo.token.claim
a21859a1-290c-4e09-85ba-627ef466263b	firstName	user.attribute
a21859a1-290c-4e09-85ba-627ef466263b	true	id.token.claim
a21859a1-290c-4e09-85ba-627ef466263b	true	access.token.claim
a21859a1-290c-4e09-85ba-627ef466263b	given_name	claim.name
a21859a1-290c-4e09-85ba-627ef466263b	String	jsonType.label
a4cc7d08-c313-40ae-a236-0a12653b2c87	true	introspection.token.claim
a4cc7d08-c313-40ae-a236-0a12653b2c87	true	userinfo.token.claim
a4cc7d08-c313-40ae-a236-0a12653b2c87	updatedAt	user.attribute
a4cc7d08-c313-40ae-a236-0a12653b2c87	true	id.token.claim
a4cc7d08-c313-40ae-a236-0a12653b2c87	true	access.token.claim
a4cc7d08-c313-40ae-a236-0a12653b2c87	updated_at	claim.name
a4cc7d08-c313-40ae-a236-0a12653b2c87	long	jsonType.label
a8d8f804-2952-4084-85ba-66454b398738	true	introspection.token.claim
a8d8f804-2952-4084-85ba-66454b398738	true	userinfo.token.claim
a8d8f804-2952-4084-85ba-66454b398738	birthdate	user.attribute
a8d8f804-2952-4084-85ba-66454b398738	true	id.token.claim
a8d8f804-2952-4084-85ba-66454b398738	true	access.token.claim
a8d8f804-2952-4084-85ba-66454b398738	birthdate	claim.name
a8d8f804-2952-4084-85ba-66454b398738	String	jsonType.label
c29b327b-451c-485b-add8-d6ebbfe6ec9a	true	introspection.token.claim
c29b327b-451c-485b-add8-d6ebbfe6ec9a	true	userinfo.token.claim
c29b327b-451c-485b-add8-d6ebbfe6ec9a	middleName	user.attribute
c29b327b-451c-485b-add8-d6ebbfe6ec9a	true	id.token.claim
c29b327b-451c-485b-add8-d6ebbfe6ec9a	true	access.token.claim
c29b327b-451c-485b-add8-d6ebbfe6ec9a	middle_name	claim.name
c29b327b-451c-485b-add8-d6ebbfe6ec9a	String	jsonType.label
ec993500-78ce-413d-b95e-ef4e11aa724b	true	introspection.token.claim
ec993500-78ce-413d-b95e-ef4e11aa724b	true	userinfo.token.claim
ec993500-78ce-413d-b95e-ef4e11aa724b	zoneinfo	user.attribute
ec993500-78ce-413d-b95e-ef4e11aa724b	true	id.token.claim
ec993500-78ce-413d-b95e-ef4e11aa724b	true	access.token.claim
ec993500-78ce-413d-b95e-ef4e11aa724b	zoneinfo	claim.name
ec993500-78ce-413d-b95e-ef4e11aa724b	String	jsonType.label
f4cb4678-2048-4e86-ac9f-2299433f7ff5	true	introspection.token.claim
f4cb4678-2048-4e86-ac9f-2299433f7ff5	true	userinfo.token.claim
f4cb4678-2048-4e86-ac9f-2299433f7ff5	profile	user.attribute
f4cb4678-2048-4e86-ac9f-2299433f7ff5	true	id.token.claim
f4cb4678-2048-4e86-ac9f-2299433f7ff5	true	access.token.claim
f4cb4678-2048-4e86-ac9f-2299433f7ff5	profile	claim.name
f4cb4678-2048-4e86-ac9f-2299433f7ff5	String	jsonType.label
66a3b444-aa3d-4a4b-a0b7-27e3a34536bd	true	introspection.token.claim
66a3b444-aa3d-4a4b-a0b7-27e3a34536bd	true	userinfo.token.claim
66a3b444-aa3d-4a4b-a0b7-27e3a34536bd	email	user.attribute
66a3b444-aa3d-4a4b-a0b7-27e3a34536bd	true	id.token.claim
66a3b444-aa3d-4a4b-a0b7-27e3a34536bd	true	access.token.claim
66a3b444-aa3d-4a4b-a0b7-27e3a34536bd	email	claim.name
66a3b444-aa3d-4a4b-a0b7-27e3a34536bd	String	jsonType.label
ebb03946-008b-443e-aff6-c6310a5e384a	true	introspection.token.claim
ebb03946-008b-443e-aff6-c6310a5e384a	true	userinfo.token.claim
ebb03946-008b-443e-aff6-c6310a5e384a	emailVerified	user.attribute
ebb03946-008b-443e-aff6-c6310a5e384a	true	id.token.claim
ebb03946-008b-443e-aff6-c6310a5e384a	true	access.token.claim
ebb03946-008b-443e-aff6-c6310a5e384a	email_verified	claim.name
ebb03946-008b-443e-aff6-c6310a5e384a	boolean	jsonType.label
6136d4fa-de96-40ea-bd5c-af7dcf8760db	formatted	user.attribute.formatted
6136d4fa-de96-40ea-bd5c-af7dcf8760db	country	user.attribute.country
6136d4fa-de96-40ea-bd5c-af7dcf8760db	true	introspection.token.claim
6136d4fa-de96-40ea-bd5c-af7dcf8760db	postal_code	user.attribute.postal_code
6136d4fa-de96-40ea-bd5c-af7dcf8760db	true	userinfo.token.claim
6136d4fa-de96-40ea-bd5c-af7dcf8760db	street	user.attribute.street
6136d4fa-de96-40ea-bd5c-af7dcf8760db	true	id.token.claim
6136d4fa-de96-40ea-bd5c-af7dcf8760db	region	user.attribute.region
6136d4fa-de96-40ea-bd5c-af7dcf8760db	true	access.token.claim
6136d4fa-de96-40ea-bd5c-af7dcf8760db	locality	user.attribute.locality
194fdf44-787e-4c5c-8c1a-74db8c2c5f31	true	introspection.token.claim
194fdf44-787e-4c5c-8c1a-74db8c2c5f31	true	userinfo.token.claim
194fdf44-787e-4c5c-8c1a-74db8c2c5f31	phoneNumberVerified	user.attribute
194fdf44-787e-4c5c-8c1a-74db8c2c5f31	true	id.token.claim
194fdf44-787e-4c5c-8c1a-74db8c2c5f31	true	access.token.claim
194fdf44-787e-4c5c-8c1a-74db8c2c5f31	phone_number_verified	claim.name
194fdf44-787e-4c5c-8c1a-74db8c2c5f31	boolean	jsonType.label
45943ed8-80ed-4d94-816a-8653e1131b84	true	introspection.token.claim
45943ed8-80ed-4d94-816a-8653e1131b84	true	userinfo.token.claim
45943ed8-80ed-4d94-816a-8653e1131b84	phoneNumber	user.attribute
45943ed8-80ed-4d94-816a-8653e1131b84	true	id.token.claim
45943ed8-80ed-4d94-816a-8653e1131b84	true	access.token.claim
45943ed8-80ed-4d94-816a-8653e1131b84	phone_number	claim.name
45943ed8-80ed-4d94-816a-8653e1131b84	String	jsonType.label
737092d4-1e96-474a-bd17-e783678bde69	true	introspection.token.claim
737092d4-1e96-474a-bd17-e783678bde69	true	multivalued
737092d4-1e96-474a-bd17-e783678bde69	foo	user.attribute
737092d4-1e96-474a-bd17-e783678bde69	true	access.token.claim
737092d4-1e96-474a-bd17-e783678bde69	realm_access.roles	claim.name
737092d4-1e96-474a-bd17-e783678bde69	String	jsonType.label
ccb7b4a7-5b7c-44e1-9fd5-4444578e869a	true	introspection.token.claim
ccb7b4a7-5b7c-44e1-9fd5-4444578e869a	true	multivalued
ccb7b4a7-5b7c-44e1-9fd5-4444578e869a	foo	user.attribute
ccb7b4a7-5b7c-44e1-9fd5-4444578e869a	true	access.token.claim
ccb7b4a7-5b7c-44e1-9fd5-4444578e869a	resource_access.${client_id}.roles	claim.name
ccb7b4a7-5b7c-44e1-9fd5-4444578e869a	String	jsonType.label
d6c29ad6-9ac5-46b1-86b2-9dca3f7f83b7	true	introspection.token.claim
d6c29ad6-9ac5-46b1-86b2-9dca3f7f83b7	true	access.token.claim
1e01a8f0-79dc-4664-a79c-76f7f8bf1a61	true	introspection.token.claim
1e01a8f0-79dc-4664-a79c-76f7f8bf1a61	true	access.token.claim
c1f28afd-8d51-4c4f-afad-c6895fbdcdb3	true	introspection.token.claim
c1f28afd-8d51-4c4f-afad-c6895fbdcdb3	true	multivalued
c1f28afd-8d51-4c4f-afad-c6895fbdcdb3	foo	user.attribute
c1f28afd-8d51-4c4f-afad-c6895fbdcdb3	true	id.token.claim
c1f28afd-8d51-4c4f-afad-c6895fbdcdb3	true	access.token.claim
c1f28afd-8d51-4c4f-afad-c6895fbdcdb3	groups	claim.name
c1f28afd-8d51-4c4f-afad-c6895fbdcdb3	String	jsonType.label
c53ba66c-1754-4c2d-881a-1911c9ef4768	true	introspection.token.claim
c53ba66c-1754-4c2d-881a-1911c9ef4768	true	userinfo.token.claim
c53ba66c-1754-4c2d-881a-1911c9ef4768	username	user.attribute
c53ba66c-1754-4c2d-881a-1911c9ef4768	true	id.token.claim
c53ba66c-1754-4c2d-881a-1911c9ef4768	true	access.token.claim
c53ba66c-1754-4c2d-881a-1911c9ef4768	upn	claim.name
c53ba66c-1754-4c2d-881a-1911c9ef4768	String	jsonType.label
977cad4a-9301-4e4a-9bb9-d2e499348346	true	introspection.token.claim
977cad4a-9301-4e4a-9bb9-d2e499348346	true	id.token.claim
977cad4a-9301-4e4a-9bb9-d2e499348346	true	access.token.claim
47dacb3d-2e4c-45b3-a003-923e62c89912	false	single
47dacb3d-2e4c-45b3-a003-923e62c89912	Basic	attribute.nameformat
47dacb3d-2e4c-45b3-a003-923e62c89912	Role	attribute.name
094833ae-1ffe-4c26-a5f4-d8d3a79f4eca	true	introspection.token.claim
094833ae-1ffe-4c26-a5f4-d8d3a79f4eca	true	userinfo.token.claim
094833ae-1ffe-4c26-a5f4-d8d3a79f4eca	website	user.attribute
094833ae-1ffe-4c26-a5f4-d8d3a79f4eca	true	id.token.claim
094833ae-1ffe-4c26-a5f4-d8d3a79f4eca	true	access.token.claim
094833ae-1ffe-4c26-a5f4-d8d3a79f4eca	website	claim.name
094833ae-1ffe-4c26-a5f4-d8d3a79f4eca	String	jsonType.label
0c66c3fd-5eb8-481e-81a2-a41568394010	true	introspection.token.claim
0c66c3fd-5eb8-481e-81a2-a41568394010	true	userinfo.token.claim
0c66c3fd-5eb8-481e-81a2-a41568394010	zoneinfo	user.attribute
0c66c3fd-5eb8-481e-81a2-a41568394010	true	id.token.claim
0c66c3fd-5eb8-481e-81a2-a41568394010	true	access.token.claim
0c66c3fd-5eb8-481e-81a2-a41568394010	zoneinfo	claim.name
0c66c3fd-5eb8-481e-81a2-a41568394010	String	jsonType.label
152b94d9-2724-435d-a075-46c164d60ff1	true	introspection.token.claim
152b94d9-2724-435d-a075-46c164d60ff1	true	userinfo.token.claim
152b94d9-2724-435d-a075-46c164d60ff1	gender	user.attribute
152b94d9-2724-435d-a075-46c164d60ff1	true	id.token.claim
152b94d9-2724-435d-a075-46c164d60ff1	true	access.token.claim
152b94d9-2724-435d-a075-46c164d60ff1	gender	claim.name
152b94d9-2724-435d-a075-46c164d60ff1	String	jsonType.label
232a3ff9-b9c3-446f-82cc-08726feb088a	true	introspection.token.claim
232a3ff9-b9c3-446f-82cc-08726feb088a	true	userinfo.token.claim
232a3ff9-b9c3-446f-82cc-08726feb088a	birthdate	user.attribute
232a3ff9-b9c3-446f-82cc-08726feb088a	true	id.token.claim
232a3ff9-b9c3-446f-82cc-08726feb088a	true	access.token.claim
232a3ff9-b9c3-446f-82cc-08726feb088a	birthdate	claim.name
232a3ff9-b9c3-446f-82cc-08726feb088a	String	jsonType.label
27ecfbdc-a4d0-4074-a8a6-704b4e273b60	true	introspection.token.claim
27ecfbdc-a4d0-4074-a8a6-704b4e273b60	true	userinfo.token.claim
27ecfbdc-a4d0-4074-a8a6-704b4e273b60	profile	user.attribute
27ecfbdc-a4d0-4074-a8a6-704b4e273b60	true	id.token.claim
27ecfbdc-a4d0-4074-a8a6-704b4e273b60	true	access.token.claim
27ecfbdc-a4d0-4074-a8a6-704b4e273b60	profile	claim.name
27ecfbdc-a4d0-4074-a8a6-704b4e273b60	String	jsonType.label
2838016e-3734-4f70-bce1-00661d942465	true	introspection.token.claim
2838016e-3734-4f70-bce1-00661d942465	true	userinfo.token.claim
2838016e-3734-4f70-bce1-00661d942465	locale	user.attribute
2838016e-3734-4f70-bce1-00661d942465	true	id.token.claim
2838016e-3734-4f70-bce1-00661d942465	true	access.token.claim
2838016e-3734-4f70-bce1-00661d942465	locale	claim.name
2838016e-3734-4f70-bce1-00661d942465	String	jsonType.label
2e08099f-fc9a-48b4-9f10-70a2fe9c05d7	true	introspection.token.claim
2e08099f-fc9a-48b4-9f10-70a2fe9c05d7	true	userinfo.token.claim
2e08099f-fc9a-48b4-9f10-70a2fe9c05d7	nickname	user.attribute
2e08099f-fc9a-48b4-9f10-70a2fe9c05d7	true	id.token.claim
2e08099f-fc9a-48b4-9f10-70a2fe9c05d7	true	access.token.claim
2e08099f-fc9a-48b4-9f10-70a2fe9c05d7	nickname	claim.name
2e08099f-fc9a-48b4-9f10-70a2fe9c05d7	String	jsonType.label
6342846a-d077-4c3b-a5db-34afc567196b	true	introspection.token.claim
6342846a-d077-4c3b-a5db-34afc567196b	true	userinfo.token.claim
6342846a-d077-4c3b-a5db-34afc567196b	updatedAt	user.attribute
6342846a-d077-4c3b-a5db-34afc567196b	true	id.token.claim
6342846a-d077-4c3b-a5db-34afc567196b	true	access.token.claim
6342846a-d077-4c3b-a5db-34afc567196b	updated_at	claim.name
6342846a-d077-4c3b-a5db-34afc567196b	long	jsonType.label
947568d2-b970-4773-b554-bfe3d5d90ff8	true	introspection.token.claim
947568d2-b970-4773-b554-bfe3d5d90ff8	true	userinfo.token.claim
947568d2-b970-4773-b554-bfe3d5d90ff8	firstName	user.attribute
947568d2-b970-4773-b554-bfe3d5d90ff8	true	id.token.claim
947568d2-b970-4773-b554-bfe3d5d90ff8	true	access.token.claim
947568d2-b970-4773-b554-bfe3d5d90ff8	given_name	claim.name
947568d2-b970-4773-b554-bfe3d5d90ff8	String	jsonType.label
c2a91f20-e55d-46fb-bb12-a8b536f6479e	true	introspection.token.claim
c2a91f20-e55d-46fb-bb12-a8b536f6479e	true	userinfo.token.claim
c2a91f20-e55d-46fb-bb12-a8b536f6479e	username	user.attribute
c2a91f20-e55d-46fb-bb12-a8b536f6479e	true	id.token.claim
c2a91f20-e55d-46fb-bb12-a8b536f6479e	true	access.token.claim
c2a91f20-e55d-46fb-bb12-a8b536f6479e	preferred_username	claim.name
c2a91f20-e55d-46fb-bb12-a8b536f6479e	String	jsonType.label
c59e006d-128b-4660-b5ac-666dcc8439d1	true	introspection.token.claim
c59e006d-128b-4660-b5ac-666dcc8439d1	true	userinfo.token.claim
c59e006d-128b-4660-b5ac-666dcc8439d1	picture	user.attribute
c59e006d-128b-4660-b5ac-666dcc8439d1	true	id.token.claim
c59e006d-128b-4660-b5ac-666dcc8439d1	true	access.token.claim
c59e006d-128b-4660-b5ac-666dcc8439d1	picture	claim.name
c59e006d-128b-4660-b5ac-666dcc8439d1	String	jsonType.label
cd7a9873-c5c4-430a-b012-d689ba802a33	true	introspection.token.claim
cd7a9873-c5c4-430a-b012-d689ba802a33	true	userinfo.token.claim
cd7a9873-c5c4-430a-b012-d689ba802a33	middleName	user.attribute
cd7a9873-c5c4-430a-b012-d689ba802a33	true	id.token.claim
cd7a9873-c5c4-430a-b012-d689ba802a33	true	access.token.claim
cd7a9873-c5c4-430a-b012-d689ba802a33	middle_name	claim.name
cd7a9873-c5c4-430a-b012-d689ba802a33	String	jsonType.label
d3851ff0-356b-45ab-83d2-79f8f5d46600	true	introspection.token.claim
d3851ff0-356b-45ab-83d2-79f8f5d46600	true	userinfo.token.claim
d3851ff0-356b-45ab-83d2-79f8f5d46600	lastName	user.attribute
d3851ff0-356b-45ab-83d2-79f8f5d46600	true	id.token.claim
d3851ff0-356b-45ab-83d2-79f8f5d46600	true	access.token.claim
d3851ff0-356b-45ab-83d2-79f8f5d46600	family_name	claim.name
d3851ff0-356b-45ab-83d2-79f8f5d46600	String	jsonType.label
f76dbe8a-d934-44bc-bc55-eb49eba23c21	true	introspection.token.claim
f76dbe8a-d934-44bc-bc55-eb49eba23c21	true	userinfo.token.claim
f76dbe8a-d934-44bc-bc55-eb49eba23c21	true	id.token.claim
f76dbe8a-d934-44bc-bc55-eb49eba23c21	true	access.token.claim
13bec266-944d-4cc9-8844-4b07f5715214	true	introspection.token.claim
13bec266-944d-4cc9-8844-4b07f5715214	true	userinfo.token.claim
13bec266-944d-4cc9-8844-4b07f5715214	emailVerified	user.attribute
13bec266-944d-4cc9-8844-4b07f5715214	true	id.token.claim
13bec266-944d-4cc9-8844-4b07f5715214	true	access.token.claim
13bec266-944d-4cc9-8844-4b07f5715214	email_verified	claim.name
13bec266-944d-4cc9-8844-4b07f5715214	boolean	jsonType.label
6a61553b-70cf-4202-812a-fbbd7b9396da	true	introspection.token.claim
6a61553b-70cf-4202-812a-fbbd7b9396da	true	userinfo.token.claim
6a61553b-70cf-4202-812a-fbbd7b9396da	email	user.attribute
6a61553b-70cf-4202-812a-fbbd7b9396da	true	id.token.claim
6a61553b-70cf-4202-812a-fbbd7b9396da	true	access.token.claim
6a61553b-70cf-4202-812a-fbbd7b9396da	email	claim.name
6a61553b-70cf-4202-812a-fbbd7b9396da	String	jsonType.label
066b9b72-2e25-4c91-82d8-823baed69972	formatted	user.attribute.formatted
066b9b72-2e25-4c91-82d8-823baed69972	country	user.attribute.country
066b9b72-2e25-4c91-82d8-823baed69972	true	introspection.token.claim
066b9b72-2e25-4c91-82d8-823baed69972	postal_code	user.attribute.postal_code
066b9b72-2e25-4c91-82d8-823baed69972	true	userinfo.token.claim
066b9b72-2e25-4c91-82d8-823baed69972	street	user.attribute.street
066b9b72-2e25-4c91-82d8-823baed69972	true	id.token.claim
066b9b72-2e25-4c91-82d8-823baed69972	region	user.attribute.region
066b9b72-2e25-4c91-82d8-823baed69972	true	access.token.claim
066b9b72-2e25-4c91-82d8-823baed69972	locality	user.attribute.locality
531563de-c62f-4836-83e2-c5c3644e45a8	true	introspection.token.claim
531563de-c62f-4836-83e2-c5c3644e45a8	true	userinfo.token.claim
531563de-c62f-4836-83e2-c5c3644e45a8	phoneNumberVerified	user.attribute
531563de-c62f-4836-83e2-c5c3644e45a8	true	id.token.claim
531563de-c62f-4836-83e2-c5c3644e45a8	true	access.token.claim
531563de-c62f-4836-83e2-c5c3644e45a8	phone_number_verified	claim.name
531563de-c62f-4836-83e2-c5c3644e45a8	boolean	jsonType.label
63aa39b8-cea9-4bd4-9a4b-d4e1b6939623	true	introspection.token.claim
63aa39b8-cea9-4bd4-9a4b-d4e1b6939623	true	userinfo.token.claim
63aa39b8-cea9-4bd4-9a4b-d4e1b6939623	phoneNumber	user.attribute
63aa39b8-cea9-4bd4-9a4b-d4e1b6939623	true	id.token.claim
63aa39b8-cea9-4bd4-9a4b-d4e1b6939623	true	access.token.claim
63aa39b8-cea9-4bd4-9a4b-d4e1b6939623	phone_number	claim.name
63aa39b8-cea9-4bd4-9a4b-d4e1b6939623	String	jsonType.label
1bfbcf35-eb71-4b15-997d-f0fe341604f3	true	introspection.token.claim
1bfbcf35-eb71-4b15-997d-f0fe341604f3	true	multivalued
1bfbcf35-eb71-4b15-997d-f0fe341604f3	foo	user.attribute
1bfbcf35-eb71-4b15-997d-f0fe341604f3	true	access.token.claim
1bfbcf35-eb71-4b15-997d-f0fe341604f3	realm_access.roles	claim.name
1bfbcf35-eb71-4b15-997d-f0fe341604f3	String	jsonType.label
610d404f-31bc-4280-a728-487f474f7291	true	introspection.token.claim
610d404f-31bc-4280-a728-487f474f7291	true	access.token.claim
f3fdb43d-afce-4112-ab96-9e703d5b21ef	true	introspection.token.claim
f3fdb43d-afce-4112-ab96-9e703d5b21ef	true	multivalued
f3fdb43d-afce-4112-ab96-9e703d5b21ef	foo	user.attribute
f3fdb43d-afce-4112-ab96-9e703d5b21ef	true	access.token.claim
f3fdb43d-afce-4112-ab96-9e703d5b21ef	resource_access.${client_id}.roles	claim.name
f3fdb43d-afce-4112-ab96-9e703d5b21ef	String	jsonType.label
e7b1ba62-b7a2-40c3-84da-6df86948daf1	true	introspection.token.claim
e7b1ba62-b7a2-40c3-84da-6df86948daf1	true	access.token.claim
adbaade4-e24f-4671-a3a9-6ae432fbe9ad	true	introspection.token.claim
adbaade4-e24f-4671-a3a9-6ae432fbe9ad	true	multivalued
adbaade4-e24f-4671-a3a9-6ae432fbe9ad	foo	user.attribute
adbaade4-e24f-4671-a3a9-6ae432fbe9ad	true	id.token.claim
adbaade4-e24f-4671-a3a9-6ae432fbe9ad	true	access.token.claim
adbaade4-e24f-4671-a3a9-6ae432fbe9ad	groups	claim.name
adbaade4-e24f-4671-a3a9-6ae432fbe9ad	String	jsonType.label
bdc766a9-f141-4b2e-a1f8-c798b29dff00	true	introspection.token.claim
bdc766a9-f141-4b2e-a1f8-c798b29dff00	true	userinfo.token.claim
bdc766a9-f141-4b2e-a1f8-c798b29dff00	username	user.attribute
bdc766a9-f141-4b2e-a1f8-c798b29dff00	true	id.token.claim
bdc766a9-f141-4b2e-a1f8-c798b29dff00	true	access.token.claim
bdc766a9-f141-4b2e-a1f8-c798b29dff00	upn	claim.name
bdc766a9-f141-4b2e-a1f8-c798b29dff00	String	jsonType.label
a4629fb2-14c2-4a21-a66f-613738e4ff7d	true	introspection.token.claim
a4629fb2-14c2-4a21-a66f-613738e4ff7d	true	id.token.claim
a4629fb2-14c2-4a21-a66f-613738e4ff7d	true	access.token.claim
888a7e60-800a-4022-abd3-695394e206b2	true	introspection.token.claim
888a7e60-800a-4022-abd3-695394e206b2	true	userinfo.token.claim
888a7e60-800a-4022-abd3-695394e206b2	locale	user.attribute
888a7e60-800a-4022-abd3-695394e206b2	true	id.token.claim
888a7e60-800a-4022-abd3-695394e206b2	true	access.token.claim
888a7e60-800a-4022-abd3-695394e206b2	locale	claim.name
888a7e60-800a-4022-abd3-695394e206b2	String	jsonType.label
7c84b23c-2fee-45c7-9f03-e1956a2d43c8	clientAddress	user.session.note
7c84b23c-2fee-45c7-9f03-e1956a2d43c8	true	introspection.token.claim
7c84b23c-2fee-45c7-9f03-e1956a2d43c8	true	id.token.claim
7c84b23c-2fee-45c7-9f03-e1956a2d43c8	true	access.token.claim
7c84b23c-2fee-45c7-9f03-e1956a2d43c8	clientAddress	claim.name
7c84b23c-2fee-45c7-9f03-e1956a2d43c8	String	jsonType.label
d50928d2-f431-4936-a583-f5c23275c71d	client_id	user.session.note
d50928d2-f431-4936-a583-f5c23275c71d	true	introspection.token.claim
d50928d2-f431-4936-a583-f5c23275c71d	true	id.token.claim
d50928d2-f431-4936-a583-f5c23275c71d	true	access.token.claim
d50928d2-f431-4936-a583-f5c23275c71d	client_id	claim.name
d50928d2-f431-4936-a583-f5c23275c71d	String	jsonType.label
fdf8062f-ce2c-48df-8e62-cf622b1a6600	clientHost	user.session.note
fdf8062f-ce2c-48df-8e62-cf622b1a6600	true	introspection.token.claim
fdf8062f-ce2c-48df-8e62-cf622b1a6600	true	id.token.claim
fdf8062f-ce2c-48df-8e62-cf622b1a6600	true	access.token.claim
fdf8062f-ce2c-48df-8e62-cf622b1a6600	clientHost	claim.name
fdf8062f-ce2c-48df-8e62-cf622b1a6600	String	jsonType.label
\.


--
-- Data for Name: realm; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.realm (id, access_code_lifespan, user_action_lifespan, access_token_lifespan, account_theme, admin_theme, email_theme, enabled, events_enabled, events_expiration, login_theme, name, not_before, password_policy, registration_allowed, remember_me, reset_password_allowed, social, ssl_required, sso_idle_timeout, sso_max_lifespan, update_profile_on_soc_login, verify_email, master_admin_client, login_lifespan, internationalization_enabled, default_locale, reg_email_as_username, admin_events_enabled, admin_events_details_enabled, edit_username_allowed, otp_policy_counter, otp_policy_window, otp_policy_period, otp_policy_digits, otp_policy_alg, otp_policy_type, browser_flow, registration_flow, direct_grant_flow, reset_credentials_flow, client_auth_flow, offline_session_idle_timeout, revoke_refresh_token, access_token_life_implicit, login_with_email_allowed, duplicate_emails_allowed, docker_auth_flow, refresh_token_max_reuse, allow_user_managed_access, sso_max_lifespan_remember_me, sso_idle_timeout_remember_me, default_role) FROM stdin;
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	60	300	300				t	t	0	ps-theme	ps-realm	0	\N	f	f	f	f	EXTERNAL	1800	36000	f	f	d72824fb-285d-4a76-ac38-cd55d99064aa	1800	f	\N	f	t	f	f	0	1	30	6	HmacSHA1	totp	e882a7e3-f4c6-4957-9413-0051eb35af41	7d461827-11cd-4218-ac62-21db69cb5cb4	42e05d04-47fd-46ac-8695-d2488426caad	cd278064-b405-49a9-b518-118680e4c780	94324de8-45ab-4312-84e3-a0835c67d5fc	2592000	f	900	t	f	a0806bdb-8462-44b1-b8cd-4d5b51e9ed52	0	f	0	0	9b8fca5f-61de-4a42-8acf-ca96ac82022c
14fac002-cb23-4725-a783-9c18edf80bf3	60	300	60	\N	\N	\N	t	f	0	\N	master	0	\N	f	f	f	f	EXTERNAL	1800	36000	f	f	ac0e21be-bc92-4767-8f0a-4ac31622d64e	1800	t	zh-CN	f	f	f	f	0	1	30	6	HmacSHA1	totp	41729bba-d3b9-48a9-9fcd-b1d69d84c4e3	59436292-2219-4fb5-9116-172c24b3de06	ad11a044-9a96-4f9d-a0f3-a7cd245a9e36	0d36c0be-cc19-4b48-ad42-8ceda3d03268	d480549e-ae7b-42d6-9f69-cd82a9037529	2592000	f	900	t	f	719d6427-f8a9-48a9-b597-c58cec100714	0	f	0	0	aa39db72-82f8-431a-abc5-a5c0b1ff9db6
\.


--
-- Data for Name: realm_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.realm_attribute (name, realm_id, value) FROM stdin;
adminEventsExpiration	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	
realmReusableOtpCode	14fac002-cb23-4725-a783-9c18edf80bf3	false
cibaBackchannelTokenDeliveryMode	14fac002-cb23-4725-a783-9c18edf80bf3	poll
cibaExpiresIn	14fac002-cb23-4725-a783-9c18edf80bf3	120
cibaAuthRequestedUserHint	14fac002-cb23-4725-a783-9c18edf80bf3	login_hint
parRequestUriLifespan	14fac002-cb23-4725-a783-9c18edf80bf3	60
cibaInterval	14fac002-cb23-4725-a783-9c18edf80bf3	5
displayName	14fac002-cb23-4725-a783-9c18edf80bf3	Keycloak
displayNameHtml	14fac002-cb23-4725-a783-9c18edf80bf3	<div class="kc-logo-text"><span>Keycloak</span></div>
bruteForceProtected	14fac002-cb23-4725-a783-9c18edf80bf3	false
permanentLockout	14fac002-cb23-4725-a783-9c18edf80bf3	false
maxFailureWaitSeconds	14fac002-cb23-4725-a783-9c18edf80bf3	900
minimumQuickLoginWaitSeconds	14fac002-cb23-4725-a783-9c18edf80bf3	60
waitIncrementSeconds	14fac002-cb23-4725-a783-9c18edf80bf3	60
quickLoginCheckMilliSeconds	14fac002-cb23-4725-a783-9c18edf80bf3	1000
maxDeltaTimeSeconds	14fac002-cb23-4725-a783-9c18edf80bf3	43200
failureFactor	14fac002-cb23-4725-a783-9c18edf80bf3	30
actionTokenGeneratedByAdminLifespan	14fac002-cb23-4725-a783-9c18edf80bf3	43200
actionTokenGeneratedByUserLifespan	14fac002-cb23-4725-a783-9c18edf80bf3	300
oauth2DeviceCodeLifespan	14fac002-cb23-4725-a783-9c18edf80bf3	600
oauth2DevicePollingInterval	14fac002-cb23-4725-a783-9c18edf80bf3	5
defaultSignatureAlgorithm	14fac002-cb23-4725-a783-9c18edf80bf3	RS256
offlineSessionMaxLifespanEnabled	14fac002-cb23-4725-a783-9c18edf80bf3	false
offlineSessionMaxLifespan	14fac002-cb23-4725-a783-9c18edf80bf3	5184000
clientSessionIdleTimeout	14fac002-cb23-4725-a783-9c18edf80bf3	0
clientSessionMaxLifespan	14fac002-cb23-4725-a783-9c18edf80bf3	0
clientOfflineSessionIdleTimeout	14fac002-cb23-4725-a783-9c18edf80bf3	0
clientOfflineSessionMaxLifespan	14fac002-cb23-4725-a783-9c18edf80bf3	0
webAuthnPolicyRpEntityName	14fac002-cb23-4725-a783-9c18edf80bf3	keycloak
webAuthnPolicySignatureAlgorithms	14fac002-cb23-4725-a783-9c18edf80bf3	ES256
webAuthnPolicyRpId	14fac002-cb23-4725-a783-9c18edf80bf3	
webAuthnPolicyAttestationConveyancePreference	14fac002-cb23-4725-a783-9c18edf80bf3	not specified
webAuthnPolicyAuthenticatorAttachment	14fac002-cb23-4725-a783-9c18edf80bf3	not specified
webAuthnPolicyRequireResidentKey	14fac002-cb23-4725-a783-9c18edf80bf3	not specified
webAuthnPolicyUserVerificationRequirement	14fac002-cb23-4725-a783-9c18edf80bf3	not specified
webAuthnPolicyCreateTimeout	14fac002-cb23-4725-a783-9c18edf80bf3	0
webAuthnPolicyAvoidSameAuthenticatorRegister	14fac002-cb23-4725-a783-9c18edf80bf3	false
webAuthnPolicyRpEntityNamePasswordless	14fac002-cb23-4725-a783-9c18edf80bf3	keycloak
webAuthnPolicySignatureAlgorithmsPasswordless	14fac002-cb23-4725-a783-9c18edf80bf3	ES256
webAuthnPolicyRpIdPasswordless	14fac002-cb23-4725-a783-9c18edf80bf3	
webAuthnPolicyAttestationConveyancePreferencePasswordless	14fac002-cb23-4725-a783-9c18edf80bf3	not specified
webAuthnPolicyAuthenticatorAttachmentPasswordless	14fac002-cb23-4725-a783-9c18edf80bf3	not specified
webAuthnPolicyRequireResidentKeyPasswordless	14fac002-cb23-4725-a783-9c18edf80bf3	not specified
webAuthnPolicyUserVerificationRequirementPasswordless	14fac002-cb23-4725-a783-9c18edf80bf3	not specified
webAuthnPolicyCreateTimeoutPasswordless	14fac002-cb23-4725-a783-9c18edf80bf3	0
webAuthnPolicyAvoidSameAuthenticatorRegisterPasswordless	14fac002-cb23-4725-a783-9c18edf80bf3	false
client-policies.profiles	14fac002-cb23-4725-a783-9c18edf80bf3	{"profiles":[]}
client-policies.policies	14fac002-cb23-4725-a783-9c18edf80bf3	{"policies":[]}
_browser_header.contentSecurityPolicyReportOnly	14fac002-cb23-4725-a783-9c18edf80bf3	
_browser_header.xContentTypeOptions	14fac002-cb23-4725-a783-9c18edf80bf3	nosniff
_browser_header.referrerPolicy	14fac002-cb23-4725-a783-9c18edf80bf3	no-referrer
_browser_header.xRobotsTag	14fac002-cb23-4725-a783-9c18edf80bf3	none
_browser_header.xFrameOptions	14fac002-cb23-4725-a783-9c18edf80bf3	SAMEORIGIN
_browser_header.xXSSProtection	14fac002-cb23-4725-a783-9c18edf80bf3	1; mode=block
_browser_header.contentSecurityPolicy	14fac002-cb23-4725-a783-9c18edf80bf3	frame-src 'self'; frame-ancestors 'self'; object-src 'none';
_browser_header.strictTransportSecurity	14fac002-cb23-4725-a783-9c18edf80bf3	max-age=31536000; includeSubDomains
realmReusableOtpCode	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	false
oauth2DeviceCodeLifespan	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	600
oauth2DevicePollingInterval	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	5
cibaBackchannelTokenDeliveryMode	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	poll
cibaExpiresIn	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	120
cibaInterval	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	5
cibaAuthRequestedUserHint	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	login_hint
parRequestUriLifespan	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	60
clientSessionIdleTimeout	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	0
clientSessionMaxLifespan	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	0
clientOfflineSessionIdleTimeout	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	0
clientOfflineSessionMaxLifespan	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	0
bruteForceProtected	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	false
permanentLockout	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	false
maxFailureWaitSeconds	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	900
minimumQuickLoginWaitSeconds	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	60
waitIncrementSeconds	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	60
quickLoginCheckMilliSeconds	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	1000
maxDeltaTimeSeconds	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	43200
failureFactor	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	30
actionTokenGeneratedByAdminLifespan	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	43200
actionTokenGeneratedByUserLifespan	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	300
defaultSignatureAlgorithm	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	RS256
offlineSessionMaxLifespanEnabled	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	false
offlineSessionMaxLifespan	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	5184000
webAuthnPolicyRpEntityName	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	keycloak
webAuthnPolicySignatureAlgorithms	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	ES256
webAuthnPolicyRpId	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	
webAuthnPolicyAttestationConveyancePreference	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	not specified
webAuthnPolicyAuthenticatorAttachment	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	not specified
webAuthnPolicyRequireResidentKey	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	not specified
webAuthnPolicyUserVerificationRequirement	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	not specified
webAuthnPolicyCreateTimeout	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	0
webAuthnPolicyAvoidSameAuthenticatorRegister	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	false
webAuthnPolicyRpEntityNamePasswordless	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	keycloak
webAuthnPolicySignatureAlgorithmsPasswordless	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	ES256
webAuthnPolicyRpIdPasswordless	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	
webAuthnPolicyAttestationConveyancePreferencePasswordless	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	not specified
webAuthnPolicyAuthenticatorAttachmentPasswordless	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	not specified
webAuthnPolicyRequireResidentKeyPasswordless	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	not specified
webAuthnPolicyUserVerificationRequirementPasswordless	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	not specified
webAuthnPolicyCreateTimeoutPasswordless	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	0
webAuthnPolicyAvoidSameAuthenticatorRegisterPasswordless	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	false
client-policies.profiles	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	{"profiles":[]}
client-policies.policies	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	{"policies":[]}
_browser_header.contentSecurityPolicyReportOnly	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	
_browser_header.xContentTypeOptions	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	nosniff
_browser_header.referrerPolicy	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	no-referrer
_browser_header.xRobotsTag	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	none
_browser_header.xFrameOptions	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	SAMEORIGIN
_browser_header.contentSecurityPolicy	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	frame-src 'self'; frame-ancestors 'self'; object-src 'none';
_browser_header.xXSSProtection	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	1; mode=block
_browser_header.strictTransportSecurity	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	max-age=31536000; includeSubDomains
\.


--
-- Data for Name: realm_default_groups; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.realm_default_groups (realm_id, group_id) FROM stdin;
\.


--
-- Data for Name: realm_enabled_event_types; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.realm_enabled_event_types (realm_id, value) FROM stdin;
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE_CONSENT_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	SEND_RESET_PASSWORD
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	GRANT_CONSENT
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	VERIFY_PROFILE_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE_TOTP
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	REMOVE_TOTP
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	REVOKE_GRANT
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	LOGIN_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CLIENT_LOGIN
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	RESET_PASSWORD_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	IMPERSONATE_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CODE_TO_TOKEN_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CUSTOM_REQUIRED_ACTION
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OAUTH2_DEVICE_CODE_TO_TOKEN_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	RESTART_AUTHENTICATION
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE_PROFILE_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	IMPERSONATE
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	LOGIN
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE_PASSWORD_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OAUTH2_DEVICE_VERIFY_USER_CODE
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CLIENT_INITIATED_ACCOUNT_LINKING
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	USER_DISABLED_BY_PERMANENT_LOCKOUT
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	TOKEN_EXCHANGE
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	REGISTER
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	LOGOUT
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	AUTHREQID_TO_TOKEN
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	DELETE_ACCOUNT_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CLIENT_REGISTER
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	IDENTITY_PROVIDER_LINK_ACCOUNT
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE_PASSWORD
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	DELETE_ACCOUNT
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	FEDERATED_IDENTITY_LINK_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CLIENT_DELETE
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	IDENTITY_PROVIDER_FIRST_LOGIN
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	VERIFY_EMAIL
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CLIENT_DELETE_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CLIENT_LOGIN_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	RESTART_AUTHENTICATION_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	REMOVE_FEDERATED_IDENTITY_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	EXECUTE_ACTIONS
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	TOKEN_EXCHANGE_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	PERMISSION_TOKEN
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	SEND_IDENTITY_PROVIDER_LINK_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	EXECUTE_ACTION_TOKEN_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	SEND_VERIFY_EMAIL
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OAUTH2_DEVICE_AUTH
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	EXECUTE_ACTIONS_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	REMOVE_FEDERATED_IDENTITY
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OAUTH2_DEVICE_CODE_TO_TOKEN
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	IDENTITY_PROVIDER_POST_LOGIN
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	IDENTITY_PROVIDER_LINK_ACCOUNT_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE_EMAIL
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OAUTH2_DEVICE_VERIFY_USER_CODE_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	REGISTER_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	REVOKE_GRANT_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	LOGOUT_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE_EMAIL_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	EXECUTE_ACTION_TOKEN
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CLIENT_UPDATE_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE_PROFILE
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	AUTHREQID_TO_TOKEN_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	FEDERATED_IDENTITY_LINK
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CLIENT_REGISTER_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	SEND_VERIFY_EMAIL_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	SEND_IDENTITY_PROVIDER_LINK
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	RESET_PASSWORD
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CLIENT_INITIATED_ACCOUNT_LINKING_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	OAUTH2_DEVICE_AUTH_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE_CONSENT
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	REMOVE_TOTP_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	VERIFY_EMAIL_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	SEND_RESET_PASSWORD_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CLIENT_UPDATE
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	IDENTITY_PROVIDER_POST_LOGIN_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CUSTOM_REQUIRED_ACTION_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	UPDATE_TOTP_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	CODE_TO_TOKEN
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	VERIFY_PROFILE
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	GRANT_CONSENT_ERROR
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	IDENTITY_PROVIDER_FIRST_LOGIN_ERROR
\.


--
-- Data for Name: realm_events_listeners; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.realm_events_listeners (realm_id, value) FROM stdin;
14fac002-cb23-4725-a783-9c18edf80bf3	jboss-logging
9d79aac4-eba0-4996-9b11-f9f8cf8b890a	jboss-logging
\.


--
-- Data for Name: realm_localizations; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.realm_localizations (realm_id, locale, texts) FROM stdin;
\.


--
-- Data for Name: realm_required_credential; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.realm_required_credential (type, form_label, input, secret, realm_id) FROM stdin;
password	password	t	t	14fac002-cb23-4725-a783-9c18edf80bf3
password	password	t	t	9d79aac4-eba0-4996-9b11-f9f8cf8b890a
\.


--
-- Data for Name: realm_smtp_config; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.realm_smtp_config (realm_id, value, name) FROM stdin;
\.


--
-- Data for Name: realm_supported_locales; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.realm_supported_locales (realm_id, value) FROM stdin;
14fac002-cb23-4725-a783-9c18edf80bf3	en
14fac002-cb23-4725-a783-9c18edf80bf3	zh-CN
\.


--
-- Data for Name: redirect_uris; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.redirect_uris (client_id, value) FROM stdin;
6959959b-71a5-48a6-862a-cee83b060340	/realms/master/account/*
f9934ba3-18fc-459e-a566-40290c557df0	/realms/master/account/*
decb93ba-5e1b-4e0d-b297-1b3f69532f47	/admin/master/console/*
d935986d-48a9-4dfd-a6ef-215644092a3f	/realms/ps-realm/account/*
136f4d7c-de2f-40e0-ba23-c3183b70c007	/realms/ps-realm/account/*
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	/admin/ps-realm/console/*
572b0c4d-ce45-4197-9815-f797e44d25d5	http://localhost:8082/ps-be/api/sso/callback
3ad38726-150e-49ce-8e3d-a627624b9af5	http://localhost:3001/sso/auth
0f933f03-43e3-4b50-8a06-ecec138f9dcd	http://localhost:3002/sso/auth
\.


--
-- Data for Name: required_action_config; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.required_action_config (required_action_id, value, name) FROM stdin;
\.


--
-- Data for Name: required_action_provider; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.required_action_provider (id, alias, name, realm_id, enabled, default_action, provider_id, priority) FROM stdin;
e9293b2a-0101-4fa0-a2f6-19d76db45868	VERIFY_EMAIL	Verify Email	14fac002-cb23-4725-a783-9c18edf80bf3	t	f	VERIFY_EMAIL	50
564cbe2e-76e5-47b6-b2a9-2da7a10b3fd8	UPDATE_PROFILE	Update Profile	14fac002-cb23-4725-a783-9c18edf80bf3	t	f	UPDATE_PROFILE	40
b5496b83-73fd-4b3b-b9cb-aee7886fdcf0	CONFIGURE_TOTP	Configure OTP	14fac002-cb23-4725-a783-9c18edf80bf3	t	f	CONFIGURE_TOTP	10
d5ca19a1-decc-4f52-b83e-28249e68f278	UPDATE_PASSWORD	Update Password	14fac002-cb23-4725-a783-9c18edf80bf3	t	f	UPDATE_PASSWORD	30
3a7e2271-29f3-4580-bb96-18d52d170e6d	TERMS_AND_CONDITIONS	Terms and Conditions	14fac002-cb23-4725-a783-9c18edf80bf3	f	f	TERMS_AND_CONDITIONS	20
c97d0216-fdb5-45f6-ae9f-1e7da1a99681	delete_account	Delete Account	14fac002-cb23-4725-a783-9c18edf80bf3	f	f	delete_account	60
6fb709be-a7b8-4fed-86ca-9e60d3045379	update_user_locale	Update User Locale	14fac002-cb23-4725-a783-9c18edf80bf3	t	f	update_user_locale	1000
38fbddb0-ad6c-4511-a424-1887288f4c2a	webauthn-register	Webauthn Register	14fac002-cb23-4725-a783-9c18edf80bf3	t	f	webauthn-register	70
027ef86e-d9db-474e-b2cc-d64f4e6ea231	webauthn-register-passwordless	Webauthn Register Passwordless	14fac002-cb23-4725-a783-9c18edf80bf3	t	f	webauthn-register-passwordless	80
03e35e5e-03b2-402a-9881-b1120cc2b5a4	VERIFY_EMAIL	Verify Email	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	t	f	VERIFY_EMAIL	50
610568d6-e3b5-427b-abaf-ebba0b13f0e0	UPDATE_PROFILE	Update Profile	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	t	f	UPDATE_PROFILE	40
a443f4b2-249a-43ec-adff-d08fb3c8b6ed	CONFIGURE_TOTP	Configure OTP	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	t	f	CONFIGURE_TOTP	10
e012c85e-2987-41b4-80f5-7dab5aa09388	UPDATE_PASSWORD	Update Password	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	t	f	UPDATE_PASSWORD	30
3b456d6c-5439-4c25-bf98-96008cabc976	TERMS_AND_CONDITIONS	Terms and Conditions	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	f	f	TERMS_AND_CONDITIONS	20
6fff09be-a05a-490c-a0c1-d54aa3bcbbd9	delete_account	Delete Account	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	f	f	delete_account	60
e148c91b-8f2c-4000-8443-07b37d49eb1a	update_user_locale	Update User Locale	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	t	f	update_user_locale	1000
37dd18c3-73a0-4e6b-a785-5ae75312668b	webauthn-register	Webauthn Register	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	t	f	webauthn-register	70
4524fd04-d83d-4244-aec5-14cafb32725a	webauthn-register-passwordless	Webauthn Register Passwordless	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	t	f	webauthn-register-passwordless	80
\.


--
-- Data for Name: resource_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.resource_attribute (id, name, value, resource_id) FROM stdin;
\.


--
-- Data for Name: resource_policy; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.resource_policy (resource_id, policy_id) FROM stdin;
\.


--
-- Data for Name: resource_scope; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.resource_scope (resource_id, scope_id) FROM stdin;
\.


--
-- Data for Name: resource_server; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.resource_server (id, allow_rs_remote_mgmt, policy_enforce_mode, decision_strategy) FROM stdin;
\.


--
-- Data for Name: resource_server_perm_ticket; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.resource_server_perm_ticket (id, owner, requester, created_timestamp, granted_timestamp, resource_id, scope_id, resource_server_id, policy_id) FROM stdin;
\.


--
-- Data for Name: resource_server_policy; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.resource_server_policy (id, name, description, type, decision_strategy, logic, resource_server_id, owner) FROM stdin;
\.


--
-- Data for Name: resource_server_resource; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.resource_server_resource (id, name, type, icon_uri, owner, resource_server_id, owner_managed_access, display_name) FROM stdin;
\.


--
-- Data for Name: resource_server_scope; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.resource_server_scope (id, name, icon_uri, resource_server_id, display_name) FROM stdin;
\.


--
-- Data for Name: resource_uris; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.resource_uris (resource_id, value) FROM stdin;
\.


--
-- Data for Name: role_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.role_attribute (id, role_id, name, value) FROM stdin;
\.


--
-- Data for Name: scope_mapping; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.scope_mapping (client_id, role_id) FROM stdin;
f9934ba3-18fc-459e-a566-40290c557df0	2b3f3938-e2ed-4676-be30-58652949f356
f9934ba3-18fc-459e-a566-40290c557df0	b8d13ee3-8a59-4da6-a545-abb269a7f7c7
136f4d7c-de2f-40e0-ba23-c3183b70c007	4adb08d1-3a97-436e-8865-c1245701906c
136f4d7c-de2f-40e0-ba23-c3183b70c007	c12f1d5f-7b61-43d8-abe9-a2d855c196a4
\.


--
-- Data for Name: scope_policy; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.scope_policy (scope_id, policy_id) FROM stdin;
\.


--
-- Data for Name: user_attribute; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_attribute (name, value, user_id, id) FROM stdin;
userId	123	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	3db6b417-7bda-40d5-89d3-1c3b72a76a26
\.


--
-- Data for Name: user_consent; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_consent (id, client_id, user_id, created_date, last_updated_date, client_storage_provider, external_client_id) FROM stdin;
\.


--
-- Data for Name: user_consent_client_scope; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_consent_client_scope (user_consent_id, scope_id) FROM stdin;
\.


--
-- Data for Name: user_entity; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_entity (id, email, email_constraint, email_verified, enabled, federation_link, first_name, last_name, realm_id, username, created_timestamp, service_account_client_link, not_before) FROM stdin;
7d21a7cd-18a4-4caa-8d5f-c1bb3d171133	\N	d9e7f8ee-c889-45dc-a7e8-8e2164cfa35f	f	t	\N	\N	\N	14fac002-cb23-4725-a783-9c18edf80bf3	admin	1757916221178	\N	0
9fb9a54c-eb53-4f67-ab8a-7d1daebadee5	\N	875486aa-fa5f-4409-9fb9-81970f716039	f	t	\N	\N	\N	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	sso1	1759029767944	\N	0
9d661917-a680-4994-a2e6-a7a5f59be4f0	\N	9756ae46-8e78-48ab-9046-25bbe4ce7085	f	t	\N	\N	\N	9d79aac4-eba0-4996-9b11-f9f8cf8b890a	service-account-fanwei	1759042365721	0f933f03-43e3-4b50-8a06-ecec138f9dcd	0
\.


--
-- Data for Name: user_federation_config; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_federation_config (user_federation_provider_id, value, name) FROM stdin;
\.


--
-- Data for Name: user_federation_mapper; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_federation_mapper (id, name, federation_provider_id, federation_mapper_type, realm_id) FROM stdin;
\.


--
-- Data for Name: user_federation_mapper_config; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_federation_mapper_config (user_federation_mapper_id, value, name) FROM stdin;
\.


--
-- Data for Name: user_federation_provider; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_federation_provider (id, changed_sync_period, display_name, full_sync_period, last_sync, priority, provider_name, realm_id) FROM stdin;
\.


--
-- Data for Name: user_group_membership; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_group_membership (group_id, user_id) FROM stdin;
\.


--
-- Data for Name: user_required_action; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_required_action (user_id, required_action) FROM stdin;
\.


--
-- Data for Name: user_role_mapping; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_role_mapping (role_id, user_id) FROM stdin;
aa39db72-82f8-431a-abc5-a5c0b1ff9db6	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
85fd5a68-0929-4cf6-a975-a479734e3b5f	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
bc9ace77-0a56-4bd9-aea0-ab14b7f89d5b	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
41e8ec6a-11e4-4f52-a2b1-662987a41f0c	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
8bf8c5ec-ef13-4fe5-a052-4108ca30336e	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
133a57c3-9e4f-4418-8548-e89ba80c331d	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
534d3622-980d-4329-b17e-b53ad3428014	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
678bc0e6-054a-42f7-ad7d-9ea4a201bdcc	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
53237d96-fbf0-44d4-aa94-a1cd03e93ab6	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
4eeafba1-202d-450a-a6fc-c2e9fbcd407e	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
b6012373-27ff-4bd6-8b5c-86c36804a2ee	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
68b257a6-fe4c-4661-bc2d-0249c445473f	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
004a94a4-545c-42a0-a2b2-6d127b1d6eab	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
0ce56b57-b985-43d8-94a7-c5a9ac2eb3c2	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
8406c6b1-beb7-4e5b-a11c-9c5c1af86363	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
74278abc-772d-44fd-85c0-fe4c3ca01d01	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
505230c6-972f-45e3-927a-6c2bc9f1d36b	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
1cf85f9f-b8e3-4d46-97c6-31b6fa944d24	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
0850b683-de4a-4374-bd30-325136f64fdc	7d21a7cd-18a4-4caa-8d5f-c1bb3d171133
9b8fca5f-61de-4a42-8acf-ca96ac82022c	9fb9a54c-eb53-4f67-ab8a-7d1daebadee5
9b8fca5f-61de-4a42-8acf-ca96ac82022c	9d661917-a680-4994-a2e6-a7a5f59be4f0
\.


--
-- Data for Name: user_session; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_session (id, auth_method, ip_address, last_session_refresh, login_username, realm_id, remember_me, started, user_id, user_session_state, broker_session_id, broker_user_id) FROM stdin;
\.


--
-- Data for Name: user_session_note; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.user_session_note (user_session, name, value) FROM stdin;
\.


--
-- Data for Name: username_login_failure; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.username_login_failure (realm_id, username, failed_login_not_before, last_failure, last_ip_failure, num_failures) FROM stdin;
\.


--
-- Data for Name: web_origins; Type: TABLE DATA; Schema: public; Owner: keycloak
--

COPY public.web_origins (client_id, value) FROM stdin;
decb93ba-5e1b-4e0d-b297-1b3f69532f47	+
9a5dd7b9-bdc1-4a33-b902-4e1af7ba4106	+
572b0c4d-ce45-4197-9815-f797e44d25d5	http://localhost:10801/*
572b0c4d-ce45-4197-9815-f797e44d25d5	http://localhost:8082/*
3ad38726-150e-49ce-8e3d-a627624b9af5	http://localhost:3001/*
0f933f03-43e3-4b50-8a06-ecec138f9dcd	http://localhost:3002/*
\.


--
-- Name: username_login_failure CONSTRAINT_17-2; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.username_login_failure
    ADD CONSTRAINT "CONSTRAINT_17-2" PRIMARY KEY (realm_id, username);


--
-- Name: keycloak_role UK_J3RWUVD56ONTGSUHOGM184WW2-2; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.keycloak_role
    ADD CONSTRAINT "UK_J3RWUVD56ONTGSUHOGM184WW2-2" UNIQUE (name, client_realm_constraint);


--
-- Name: client_auth_flow_bindings c_cli_flow_bind; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_auth_flow_bindings
    ADD CONSTRAINT c_cli_flow_bind PRIMARY KEY (client_id, binding_name);


--
-- Name: client_scope_client c_cli_scope_bind; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_scope_client
    ADD CONSTRAINT c_cli_scope_bind PRIMARY KEY (client_id, scope_id);


--
-- Name: client_initial_access cnstr_client_init_acc_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_initial_access
    ADD CONSTRAINT cnstr_client_init_acc_pk PRIMARY KEY (id);


--
-- Name: realm_default_groups con_group_id_def_groups; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_default_groups
    ADD CONSTRAINT con_group_id_def_groups UNIQUE (group_id);


--
-- Name: broker_link constr_broker_link_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.broker_link
    ADD CONSTRAINT constr_broker_link_pk PRIMARY KEY (identity_provider, user_id);


--
-- Name: client_user_session_note constr_cl_usr_ses_note; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_user_session_note
    ADD CONSTRAINT constr_cl_usr_ses_note PRIMARY KEY (client_session, name);


--
-- Name: component_config constr_component_config_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.component_config
    ADD CONSTRAINT constr_component_config_pk PRIMARY KEY (id);


--
-- Name: component constr_component_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.component
    ADD CONSTRAINT constr_component_pk PRIMARY KEY (id);


--
-- Name: fed_user_required_action constr_fed_required_action; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.fed_user_required_action
    ADD CONSTRAINT constr_fed_required_action PRIMARY KEY (required_action, user_id);


--
-- Name: fed_user_attribute constr_fed_user_attr_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.fed_user_attribute
    ADD CONSTRAINT constr_fed_user_attr_pk PRIMARY KEY (id);


--
-- Name: fed_user_consent constr_fed_user_consent_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.fed_user_consent
    ADD CONSTRAINT constr_fed_user_consent_pk PRIMARY KEY (id);


--
-- Name: fed_user_credential constr_fed_user_cred_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.fed_user_credential
    ADD CONSTRAINT constr_fed_user_cred_pk PRIMARY KEY (id);


--
-- Name: fed_user_group_membership constr_fed_user_group; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.fed_user_group_membership
    ADD CONSTRAINT constr_fed_user_group PRIMARY KEY (group_id, user_id);


--
-- Name: fed_user_role_mapping constr_fed_user_role; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.fed_user_role_mapping
    ADD CONSTRAINT constr_fed_user_role PRIMARY KEY (role_id, user_id);


--
-- Name: federated_user constr_federated_user; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.federated_user
    ADD CONSTRAINT constr_federated_user PRIMARY KEY (id);


--
-- Name: realm_default_groups constr_realm_default_groups; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_default_groups
    ADD CONSTRAINT constr_realm_default_groups PRIMARY KEY (realm_id, group_id);


--
-- Name: realm_enabled_event_types constr_realm_enabl_event_types; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_enabled_event_types
    ADD CONSTRAINT constr_realm_enabl_event_types PRIMARY KEY (realm_id, value);


--
-- Name: realm_events_listeners constr_realm_events_listeners; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_events_listeners
    ADD CONSTRAINT constr_realm_events_listeners PRIMARY KEY (realm_id, value);


--
-- Name: realm_supported_locales constr_realm_supported_locales; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_supported_locales
    ADD CONSTRAINT constr_realm_supported_locales PRIMARY KEY (realm_id, value);


--
-- Name: identity_provider constraint_2b; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.identity_provider
    ADD CONSTRAINT constraint_2b PRIMARY KEY (internal_id);


--
-- Name: client_attributes constraint_3c; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_attributes
    ADD CONSTRAINT constraint_3c PRIMARY KEY (client_id, name);


--
-- Name: event_entity constraint_4; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.event_entity
    ADD CONSTRAINT constraint_4 PRIMARY KEY (id);


--
-- Name: federated_identity constraint_40; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.federated_identity
    ADD CONSTRAINT constraint_40 PRIMARY KEY (identity_provider, user_id);


--
-- Name: realm constraint_4a; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm
    ADD CONSTRAINT constraint_4a PRIMARY KEY (id);


--
-- Name: client_session_role constraint_5; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_session_role
    ADD CONSTRAINT constraint_5 PRIMARY KEY (client_session, role_id);


--
-- Name: user_session constraint_57; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_session
    ADD CONSTRAINT constraint_57 PRIMARY KEY (id);


--
-- Name: user_federation_provider constraint_5c; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_federation_provider
    ADD CONSTRAINT constraint_5c PRIMARY KEY (id);


--
-- Name: client_session_note constraint_5e; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_session_note
    ADD CONSTRAINT constraint_5e PRIMARY KEY (client_session, name);


--
-- Name: client constraint_7; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT constraint_7 PRIMARY KEY (id);


--
-- Name: client_session constraint_8; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_session
    ADD CONSTRAINT constraint_8 PRIMARY KEY (id);


--
-- Name: scope_mapping constraint_81; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.scope_mapping
    ADD CONSTRAINT constraint_81 PRIMARY KEY (client_id, role_id);


--
-- Name: client_node_registrations constraint_84; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_node_registrations
    ADD CONSTRAINT constraint_84 PRIMARY KEY (client_id, name);


--
-- Name: realm_attribute constraint_9; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_attribute
    ADD CONSTRAINT constraint_9 PRIMARY KEY (name, realm_id);


--
-- Name: realm_required_credential constraint_92; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_required_credential
    ADD CONSTRAINT constraint_92 PRIMARY KEY (realm_id, type);


--
-- Name: keycloak_role constraint_a; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.keycloak_role
    ADD CONSTRAINT constraint_a PRIMARY KEY (id);


--
-- Name: admin_event_entity constraint_admin_event_entity; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.admin_event_entity
    ADD CONSTRAINT constraint_admin_event_entity PRIMARY KEY (id);


--
-- Name: authenticator_config_entry constraint_auth_cfg_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.authenticator_config_entry
    ADD CONSTRAINT constraint_auth_cfg_pk PRIMARY KEY (authenticator_id, name);


--
-- Name: authentication_execution constraint_auth_exec_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.authentication_execution
    ADD CONSTRAINT constraint_auth_exec_pk PRIMARY KEY (id);


--
-- Name: authentication_flow constraint_auth_flow_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.authentication_flow
    ADD CONSTRAINT constraint_auth_flow_pk PRIMARY KEY (id);


--
-- Name: authenticator_config constraint_auth_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.authenticator_config
    ADD CONSTRAINT constraint_auth_pk PRIMARY KEY (id);


--
-- Name: client_session_auth_status constraint_auth_status_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_session_auth_status
    ADD CONSTRAINT constraint_auth_status_pk PRIMARY KEY (client_session, authenticator);


--
-- Name: user_role_mapping constraint_c; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_role_mapping
    ADD CONSTRAINT constraint_c PRIMARY KEY (role_id, user_id);


--
-- Name: composite_role constraint_composite_role; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.composite_role
    ADD CONSTRAINT constraint_composite_role PRIMARY KEY (composite, child_role);


--
-- Name: client_session_prot_mapper constraint_cs_pmp_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_session_prot_mapper
    ADD CONSTRAINT constraint_cs_pmp_pk PRIMARY KEY (client_session, protocol_mapper_id);


--
-- Name: identity_provider_config constraint_d; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.identity_provider_config
    ADD CONSTRAINT constraint_d PRIMARY KEY (identity_provider_id, name);


--
-- Name: policy_config constraint_dpc; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.policy_config
    ADD CONSTRAINT constraint_dpc PRIMARY KEY (policy_id, name);


--
-- Name: realm_smtp_config constraint_e; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_smtp_config
    ADD CONSTRAINT constraint_e PRIMARY KEY (realm_id, name);


--
-- Name: credential constraint_f; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.credential
    ADD CONSTRAINT constraint_f PRIMARY KEY (id);


--
-- Name: user_federation_config constraint_f9; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_federation_config
    ADD CONSTRAINT constraint_f9 PRIMARY KEY (user_federation_provider_id, name);


--
-- Name: resource_server_perm_ticket constraint_fapmt; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT constraint_fapmt PRIMARY KEY (id);


--
-- Name: resource_server_resource constraint_farsr; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_resource
    ADD CONSTRAINT constraint_farsr PRIMARY KEY (id);


--
-- Name: resource_server_policy constraint_farsrp; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_policy
    ADD CONSTRAINT constraint_farsrp PRIMARY KEY (id);


--
-- Name: associated_policy constraint_farsrpap; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.associated_policy
    ADD CONSTRAINT constraint_farsrpap PRIMARY KEY (policy_id, associated_policy_id);


--
-- Name: resource_policy constraint_farsrpp; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_policy
    ADD CONSTRAINT constraint_farsrpp PRIMARY KEY (resource_id, policy_id);


--
-- Name: resource_server_scope constraint_farsrs; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_scope
    ADD CONSTRAINT constraint_farsrs PRIMARY KEY (id);


--
-- Name: resource_scope constraint_farsrsp; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_scope
    ADD CONSTRAINT constraint_farsrsp PRIMARY KEY (resource_id, scope_id);


--
-- Name: scope_policy constraint_farsrsps; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.scope_policy
    ADD CONSTRAINT constraint_farsrsps PRIMARY KEY (scope_id, policy_id);


--
-- Name: user_entity constraint_fb; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT constraint_fb PRIMARY KEY (id);


--
-- Name: user_federation_mapper_config constraint_fedmapper_cfg_pm; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_federation_mapper_config
    ADD CONSTRAINT constraint_fedmapper_cfg_pm PRIMARY KEY (user_federation_mapper_id, name);


--
-- Name: user_federation_mapper constraint_fedmapperpm; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_federation_mapper
    ADD CONSTRAINT constraint_fedmapperpm PRIMARY KEY (id);


--
-- Name: fed_user_consent_cl_scope constraint_fgrntcsnt_clsc_pm; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.fed_user_consent_cl_scope
    ADD CONSTRAINT constraint_fgrntcsnt_clsc_pm PRIMARY KEY (user_consent_id, scope_id);


--
-- Name: user_consent_client_scope constraint_grntcsnt_clsc_pm; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_consent_client_scope
    ADD CONSTRAINT constraint_grntcsnt_clsc_pm PRIMARY KEY (user_consent_id, scope_id);


--
-- Name: user_consent constraint_grntcsnt_pm; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_consent
    ADD CONSTRAINT constraint_grntcsnt_pm PRIMARY KEY (id);


--
-- Name: keycloak_group constraint_group; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.keycloak_group
    ADD CONSTRAINT constraint_group PRIMARY KEY (id);


--
-- Name: group_attribute constraint_group_attribute_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.group_attribute
    ADD CONSTRAINT constraint_group_attribute_pk PRIMARY KEY (id);


--
-- Name: group_role_mapping constraint_group_role; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.group_role_mapping
    ADD CONSTRAINT constraint_group_role PRIMARY KEY (role_id, group_id);


--
-- Name: identity_provider_mapper constraint_idpm; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.identity_provider_mapper
    ADD CONSTRAINT constraint_idpm PRIMARY KEY (id);


--
-- Name: idp_mapper_config constraint_idpmconfig; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.idp_mapper_config
    ADD CONSTRAINT constraint_idpmconfig PRIMARY KEY (idp_mapper_id, name);


--
-- Name: migration_model constraint_migmod; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.migration_model
    ADD CONSTRAINT constraint_migmod PRIMARY KEY (id);


--
-- Name: offline_client_session constraint_offl_cl_ses_pk3; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.offline_client_session
    ADD CONSTRAINT constraint_offl_cl_ses_pk3 PRIMARY KEY (user_session_id, client_id, client_storage_provider, external_client_id, offline_flag);


--
-- Name: offline_user_session constraint_offl_us_ses_pk2; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.offline_user_session
    ADD CONSTRAINT constraint_offl_us_ses_pk2 PRIMARY KEY (user_session_id, offline_flag);


--
-- Name: protocol_mapper constraint_pcm; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.protocol_mapper
    ADD CONSTRAINT constraint_pcm PRIMARY KEY (id);


--
-- Name: protocol_mapper_config constraint_pmconfig; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.protocol_mapper_config
    ADD CONSTRAINT constraint_pmconfig PRIMARY KEY (protocol_mapper_id, name);


--
-- Name: redirect_uris constraint_redirect_uris; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.redirect_uris
    ADD CONSTRAINT constraint_redirect_uris PRIMARY KEY (client_id, value);


--
-- Name: required_action_config constraint_req_act_cfg_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.required_action_config
    ADD CONSTRAINT constraint_req_act_cfg_pk PRIMARY KEY (required_action_id, name);


--
-- Name: required_action_provider constraint_req_act_prv_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.required_action_provider
    ADD CONSTRAINT constraint_req_act_prv_pk PRIMARY KEY (id);


--
-- Name: user_required_action constraint_required_action; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_required_action
    ADD CONSTRAINT constraint_required_action PRIMARY KEY (required_action, user_id);


--
-- Name: resource_uris constraint_resour_uris_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_uris
    ADD CONSTRAINT constraint_resour_uris_pk PRIMARY KEY (resource_id, value);


--
-- Name: role_attribute constraint_role_attribute_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.role_attribute
    ADD CONSTRAINT constraint_role_attribute_pk PRIMARY KEY (id);


--
-- Name: user_attribute constraint_user_attribute_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_attribute
    ADD CONSTRAINT constraint_user_attribute_pk PRIMARY KEY (id);


--
-- Name: user_group_membership constraint_user_group; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_group_membership
    ADD CONSTRAINT constraint_user_group PRIMARY KEY (group_id, user_id);


--
-- Name: user_session_note constraint_usn_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_session_note
    ADD CONSTRAINT constraint_usn_pk PRIMARY KEY (user_session, name);


--
-- Name: web_origins constraint_web_origins; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.web_origins
    ADD CONSTRAINT constraint_web_origins PRIMARY KEY (client_id, value);


--
-- Name: databasechangeloglock databasechangeloglock_pkey; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.databasechangeloglock
    ADD CONSTRAINT databasechangeloglock_pkey PRIMARY KEY (id);


--
-- Name: client_scope_attributes pk_cl_tmpl_attr; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_scope_attributes
    ADD CONSTRAINT pk_cl_tmpl_attr PRIMARY KEY (scope_id, name);


--
-- Name: client_scope pk_cli_template; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_scope
    ADD CONSTRAINT pk_cli_template PRIMARY KEY (id);


--
-- Name: resource_server pk_resource_server; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server
    ADD CONSTRAINT pk_resource_server PRIMARY KEY (id);


--
-- Name: client_scope_role_mapping pk_template_scope; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_scope_role_mapping
    ADD CONSTRAINT pk_template_scope PRIMARY KEY (scope_id, role_id);


--
-- Name: default_client_scope r_def_cli_scope_bind; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.default_client_scope
    ADD CONSTRAINT r_def_cli_scope_bind PRIMARY KEY (realm_id, scope_id);


--
-- Name: realm_localizations realm_localizations_pkey; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_localizations
    ADD CONSTRAINT realm_localizations_pkey PRIMARY KEY (realm_id, locale);


--
-- Name: resource_attribute res_attr_pk; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_attribute
    ADD CONSTRAINT res_attr_pk PRIMARY KEY (id);


--
-- Name: keycloak_group sibling_names; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.keycloak_group
    ADD CONSTRAINT sibling_names UNIQUE (realm_id, parent_group, name);


--
-- Name: identity_provider uk_2daelwnibji49avxsrtuf6xj33; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.identity_provider
    ADD CONSTRAINT uk_2daelwnibji49avxsrtuf6xj33 UNIQUE (provider_alias, realm_id);


--
-- Name: client uk_b71cjlbenv945rb6gcon438at; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client
    ADD CONSTRAINT uk_b71cjlbenv945rb6gcon438at UNIQUE (realm_id, client_id);


--
-- Name: client_scope uk_cli_scope; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_scope
    ADD CONSTRAINT uk_cli_scope UNIQUE (realm_id, name);


--
-- Name: user_entity uk_dykn684sl8up1crfei6eckhd7; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT uk_dykn684sl8up1crfei6eckhd7 UNIQUE (realm_id, email_constraint);


--
-- Name: resource_server_resource uk_frsr6t700s9v50bu18ws5ha6; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_resource
    ADD CONSTRAINT uk_frsr6t700s9v50bu18ws5ha6 UNIQUE (name, owner, resource_server_id);


--
-- Name: resource_server_perm_ticket uk_frsr6t700s9v50bu18ws5pmt; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT uk_frsr6t700s9v50bu18ws5pmt UNIQUE (owner, requester, resource_server_id, resource_id, scope_id);


--
-- Name: resource_server_policy uk_frsrpt700s9v50bu18ws5ha6; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_policy
    ADD CONSTRAINT uk_frsrpt700s9v50bu18ws5ha6 UNIQUE (name, resource_server_id);


--
-- Name: resource_server_scope uk_frsrst700s9v50bu18ws5ha6; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_scope
    ADD CONSTRAINT uk_frsrst700s9v50bu18ws5ha6 UNIQUE (name, resource_server_id);


--
-- Name: user_consent uk_jkuwuvd56ontgsuhogm8uewrt; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_consent
    ADD CONSTRAINT uk_jkuwuvd56ontgsuhogm8uewrt UNIQUE (client_id, client_storage_provider, external_client_id, user_id);


--
-- Name: realm uk_orvsdmla56612eaefiq6wl5oi; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm
    ADD CONSTRAINT uk_orvsdmla56612eaefiq6wl5oi UNIQUE (name);


--
-- Name: user_entity uk_ru8tt6t700s9v50bu18ws5ha6; Type: CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_entity
    ADD CONSTRAINT uk_ru8tt6t700s9v50bu18ws5ha6 UNIQUE (realm_id, username);


--
-- Name: idx_admin_event_time; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_admin_event_time ON public.admin_event_entity USING btree (realm_id, admin_event_time);


--
-- Name: idx_assoc_pol_assoc_pol_id; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_assoc_pol_assoc_pol_id ON public.associated_policy USING btree (associated_policy_id);


--
-- Name: idx_auth_config_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_auth_config_realm ON public.authenticator_config USING btree (realm_id);


--
-- Name: idx_auth_exec_flow; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_auth_exec_flow ON public.authentication_execution USING btree (flow_id);


--
-- Name: idx_auth_exec_realm_flow; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_auth_exec_realm_flow ON public.authentication_execution USING btree (realm_id, flow_id);


--
-- Name: idx_auth_flow_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_auth_flow_realm ON public.authentication_flow USING btree (realm_id);


--
-- Name: idx_cl_clscope; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_cl_clscope ON public.client_scope_client USING btree (scope_id);


--
-- Name: idx_client_id; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_client_id ON public.client USING btree (client_id);


--
-- Name: idx_client_init_acc_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_client_init_acc_realm ON public.client_initial_access USING btree (realm_id);


--
-- Name: idx_client_session_session; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_client_session_session ON public.client_session USING btree (session_id);


--
-- Name: idx_clscope_attrs; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_clscope_attrs ON public.client_scope_attributes USING btree (scope_id);


--
-- Name: idx_clscope_cl; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_clscope_cl ON public.client_scope_client USING btree (client_id);


--
-- Name: idx_clscope_protmap; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_clscope_protmap ON public.protocol_mapper USING btree (client_scope_id);


--
-- Name: idx_clscope_role; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_clscope_role ON public.client_scope_role_mapping USING btree (scope_id);


--
-- Name: idx_compo_config_compo; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_compo_config_compo ON public.component_config USING btree (component_id);


--
-- Name: idx_component_provider_type; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_component_provider_type ON public.component USING btree (provider_type);


--
-- Name: idx_component_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_component_realm ON public.component USING btree (realm_id);


--
-- Name: idx_composite; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_composite ON public.composite_role USING btree (composite);


--
-- Name: idx_composite_child; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_composite_child ON public.composite_role USING btree (child_role);


--
-- Name: idx_defcls_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_defcls_realm ON public.default_client_scope USING btree (realm_id);


--
-- Name: idx_defcls_scope; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_defcls_scope ON public.default_client_scope USING btree (scope_id);


--
-- Name: idx_event_time; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_event_time ON public.event_entity USING btree (realm_id, event_time);


--
-- Name: idx_fedidentity_feduser; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fedidentity_feduser ON public.federated_identity USING btree (federated_user_id);


--
-- Name: idx_fedidentity_user; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fedidentity_user ON public.federated_identity USING btree (user_id);


--
-- Name: idx_fu_attribute; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_attribute ON public.fed_user_attribute USING btree (user_id, realm_id, name);


--
-- Name: idx_fu_cnsnt_ext; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_cnsnt_ext ON public.fed_user_consent USING btree (user_id, client_storage_provider, external_client_id);


--
-- Name: idx_fu_consent; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_consent ON public.fed_user_consent USING btree (user_id, client_id);


--
-- Name: idx_fu_consent_ru; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_consent_ru ON public.fed_user_consent USING btree (realm_id, user_id);


--
-- Name: idx_fu_credential; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_credential ON public.fed_user_credential USING btree (user_id, type);


--
-- Name: idx_fu_credential_ru; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_credential_ru ON public.fed_user_credential USING btree (realm_id, user_id);


--
-- Name: idx_fu_group_membership; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_group_membership ON public.fed_user_group_membership USING btree (user_id, group_id);


--
-- Name: idx_fu_group_membership_ru; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_group_membership_ru ON public.fed_user_group_membership USING btree (realm_id, user_id);


--
-- Name: idx_fu_required_action; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_required_action ON public.fed_user_required_action USING btree (user_id, required_action);


--
-- Name: idx_fu_required_action_ru; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_required_action_ru ON public.fed_user_required_action USING btree (realm_id, user_id);


--
-- Name: idx_fu_role_mapping; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_role_mapping ON public.fed_user_role_mapping USING btree (user_id, role_id);


--
-- Name: idx_fu_role_mapping_ru; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_fu_role_mapping_ru ON public.fed_user_role_mapping USING btree (realm_id, user_id);


--
-- Name: idx_group_att_by_name_value; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_group_att_by_name_value ON public.group_attribute USING btree (name, ((value)::character varying(250)));


--
-- Name: idx_group_attr_group; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_group_attr_group ON public.group_attribute USING btree (group_id);


--
-- Name: idx_group_role_mapp_group; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_group_role_mapp_group ON public.group_role_mapping USING btree (group_id);


--
-- Name: idx_id_prov_mapp_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_id_prov_mapp_realm ON public.identity_provider_mapper USING btree (realm_id);


--
-- Name: idx_ident_prov_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_ident_prov_realm ON public.identity_provider USING btree (realm_id);


--
-- Name: idx_keycloak_role_client; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_keycloak_role_client ON public.keycloak_role USING btree (client);


--
-- Name: idx_keycloak_role_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_keycloak_role_realm ON public.keycloak_role USING btree (realm);


--
-- Name: idx_offline_css_preload; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_offline_css_preload ON public.offline_client_session USING btree (client_id, offline_flag);


--
-- Name: idx_offline_uss_by_user; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_offline_uss_by_user ON public.offline_user_session USING btree (user_id, realm_id, offline_flag);


--
-- Name: idx_offline_uss_by_usersess; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_offline_uss_by_usersess ON public.offline_user_session USING btree (realm_id, offline_flag, user_session_id);


--
-- Name: idx_offline_uss_createdon; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_offline_uss_createdon ON public.offline_user_session USING btree (created_on);


--
-- Name: idx_offline_uss_preload; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_offline_uss_preload ON public.offline_user_session USING btree (offline_flag, created_on, user_session_id);


--
-- Name: idx_protocol_mapper_client; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_protocol_mapper_client ON public.protocol_mapper USING btree (client_id);


--
-- Name: idx_realm_attr_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_realm_attr_realm ON public.realm_attribute USING btree (realm_id);


--
-- Name: idx_realm_clscope; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_realm_clscope ON public.client_scope USING btree (realm_id);


--
-- Name: idx_realm_def_grp_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_realm_def_grp_realm ON public.realm_default_groups USING btree (realm_id);


--
-- Name: idx_realm_evt_list_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_realm_evt_list_realm ON public.realm_events_listeners USING btree (realm_id);


--
-- Name: idx_realm_evt_types_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_realm_evt_types_realm ON public.realm_enabled_event_types USING btree (realm_id);


--
-- Name: idx_realm_master_adm_cli; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_realm_master_adm_cli ON public.realm USING btree (master_admin_client);


--
-- Name: idx_realm_supp_local_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_realm_supp_local_realm ON public.realm_supported_locales USING btree (realm_id);


--
-- Name: idx_redir_uri_client; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_redir_uri_client ON public.redirect_uris USING btree (client_id);


--
-- Name: idx_req_act_prov_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_req_act_prov_realm ON public.required_action_provider USING btree (realm_id);


--
-- Name: idx_res_policy_policy; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_res_policy_policy ON public.resource_policy USING btree (policy_id);


--
-- Name: idx_res_scope_scope; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_res_scope_scope ON public.resource_scope USING btree (scope_id);


--
-- Name: idx_res_serv_pol_res_serv; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_res_serv_pol_res_serv ON public.resource_server_policy USING btree (resource_server_id);


--
-- Name: idx_res_srv_res_res_srv; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_res_srv_res_res_srv ON public.resource_server_resource USING btree (resource_server_id);


--
-- Name: idx_res_srv_scope_res_srv; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_res_srv_scope_res_srv ON public.resource_server_scope USING btree (resource_server_id);


--
-- Name: idx_role_attribute; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_role_attribute ON public.role_attribute USING btree (role_id);


--
-- Name: idx_role_clscope; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_role_clscope ON public.client_scope_role_mapping USING btree (role_id);


--
-- Name: idx_scope_mapping_role; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_scope_mapping_role ON public.scope_mapping USING btree (role_id);


--
-- Name: idx_scope_policy_policy; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_scope_policy_policy ON public.scope_policy USING btree (policy_id);


--
-- Name: idx_update_time; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_update_time ON public.migration_model USING btree (update_time);


--
-- Name: idx_us_sess_id_on_cl_sess; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_us_sess_id_on_cl_sess ON public.offline_client_session USING btree (user_session_id);


--
-- Name: idx_usconsent_clscope; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_usconsent_clscope ON public.user_consent_client_scope USING btree (user_consent_id);


--
-- Name: idx_user_attribute; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_user_attribute ON public.user_attribute USING btree (user_id);


--
-- Name: idx_user_attribute_name; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_user_attribute_name ON public.user_attribute USING btree (name, value);


--
-- Name: idx_user_consent; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_user_consent ON public.user_consent USING btree (user_id);


--
-- Name: idx_user_credential; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_user_credential ON public.credential USING btree (user_id);


--
-- Name: idx_user_email; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_user_email ON public.user_entity USING btree (email);


--
-- Name: idx_user_group_mapping; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_user_group_mapping ON public.user_group_membership USING btree (user_id);


--
-- Name: idx_user_reqactions; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_user_reqactions ON public.user_required_action USING btree (user_id);


--
-- Name: idx_user_role_mapping; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_user_role_mapping ON public.user_role_mapping USING btree (user_id);


--
-- Name: idx_user_service_account; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_user_service_account ON public.user_entity USING btree (realm_id, service_account_client_link);


--
-- Name: idx_usr_fed_map_fed_prv; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_usr_fed_map_fed_prv ON public.user_federation_mapper USING btree (federation_provider_id);


--
-- Name: idx_usr_fed_map_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_usr_fed_map_realm ON public.user_federation_mapper USING btree (realm_id);


--
-- Name: idx_usr_fed_prv_realm; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_usr_fed_prv_realm ON public.user_federation_provider USING btree (realm_id);


--
-- Name: idx_web_orig_client; Type: INDEX; Schema: public; Owner: keycloak
--

CREATE INDEX idx_web_orig_client ON public.web_origins USING btree (client_id);


--
-- Name: client_session_auth_status auth_status_constraint; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_session_auth_status
    ADD CONSTRAINT auth_status_constraint FOREIGN KEY (client_session) REFERENCES public.client_session(id);


--
-- Name: identity_provider fk2b4ebc52ae5c3b34; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.identity_provider
    ADD CONSTRAINT fk2b4ebc52ae5c3b34 FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: client_attributes fk3c47c64beacca966; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_attributes
    ADD CONSTRAINT fk3c47c64beacca966 FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- Name: federated_identity fk404288b92ef007a6; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.federated_identity
    ADD CONSTRAINT fk404288b92ef007a6 FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- Name: client_node_registrations fk4129723ba992f594; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_node_registrations
    ADD CONSTRAINT fk4129723ba992f594 FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- Name: client_session_note fk5edfb00ff51c2736; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_session_note
    ADD CONSTRAINT fk5edfb00ff51c2736 FOREIGN KEY (client_session) REFERENCES public.client_session(id);


--
-- Name: user_session_note fk5edfb00ff51d3472; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_session_note
    ADD CONSTRAINT fk5edfb00ff51d3472 FOREIGN KEY (user_session) REFERENCES public.user_session(id);


--
-- Name: client_session_role fk_11b7sgqw18i532811v7o2dv76; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_session_role
    ADD CONSTRAINT fk_11b7sgqw18i532811v7o2dv76 FOREIGN KEY (client_session) REFERENCES public.client_session(id);


--
-- Name: redirect_uris fk_1burs8pb4ouj97h5wuppahv9f; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.redirect_uris
    ADD CONSTRAINT fk_1burs8pb4ouj97h5wuppahv9f FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- Name: user_federation_provider fk_1fj32f6ptolw2qy60cd8n01e8; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_federation_provider
    ADD CONSTRAINT fk_1fj32f6ptolw2qy60cd8n01e8 FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: client_session_prot_mapper fk_33a8sgqw18i532811v7o2dk89; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_session_prot_mapper
    ADD CONSTRAINT fk_33a8sgqw18i532811v7o2dk89 FOREIGN KEY (client_session) REFERENCES public.client_session(id);


--
-- Name: realm_required_credential fk_5hg65lybevavkqfki3kponh9v; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_required_credential
    ADD CONSTRAINT fk_5hg65lybevavkqfki3kponh9v FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: resource_attribute fk_5hrm2vlf9ql5fu022kqepovbr; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_attribute
    ADD CONSTRAINT fk_5hrm2vlf9ql5fu022kqepovbr FOREIGN KEY (resource_id) REFERENCES public.resource_server_resource(id);


--
-- Name: user_attribute fk_5hrm2vlf9ql5fu043kqepovbr; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_attribute
    ADD CONSTRAINT fk_5hrm2vlf9ql5fu043kqepovbr FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- Name: user_required_action fk_6qj3w1jw9cvafhe19bwsiuvmd; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_required_action
    ADD CONSTRAINT fk_6qj3w1jw9cvafhe19bwsiuvmd FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- Name: keycloak_role fk_6vyqfe4cn4wlq8r6kt5vdsj5c; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.keycloak_role
    ADD CONSTRAINT fk_6vyqfe4cn4wlq8r6kt5vdsj5c FOREIGN KEY (realm) REFERENCES public.realm(id);


--
-- Name: realm_smtp_config fk_70ej8xdxgxd0b9hh6180irr0o; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_smtp_config
    ADD CONSTRAINT fk_70ej8xdxgxd0b9hh6180irr0o FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: realm_attribute fk_8shxd6l3e9atqukacxgpffptw; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_attribute
    ADD CONSTRAINT fk_8shxd6l3e9atqukacxgpffptw FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: composite_role fk_a63wvekftu8jo1pnj81e7mce2; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.composite_role
    ADD CONSTRAINT fk_a63wvekftu8jo1pnj81e7mce2 FOREIGN KEY (composite) REFERENCES public.keycloak_role(id);


--
-- Name: authentication_execution fk_auth_exec_flow; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.authentication_execution
    ADD CONSTRAINT fk_auth_exec_flow FOREIGN KEY (flow_id) REFERENCES public.authentication_flow(id);


--
-- Name: authentication_execution fk_auth_exec_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.authentication_execution
    ADD CONSTRAINT fk_auth_exec_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: authentication_flow fk_auth_flow_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.authentication_flow
    ADD CONSTRAINT fk_auth_flow_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: authenticator_config fk_auth_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.authenticator_config
    ADD CONSTRAINT fk_auth_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: client_session fk_b4ao2vcvat6ukau74wbwtfqo1; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_session
    ADD CONSTRAINT fk_b4ao2vcvat6ukau74wbwtfqo1 FOREIGN KEY (session_id) REFERENCES public.user_session(id);


--
-- Name: user_role_mapping fk_c4fqv34p1mbylloxang7b1q3l; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_role_mapping
    ADD CONSTRAINT fk_c4fqv34p1mbylloxang7b1q3l FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- Name: client_scope_attributes fk_cl_scope_attr_scope; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_scope_attributes
    ADD CONSTRAINT fk_cl_scope_attr_scope FOREIGN KEY (scope_id) REFERENCES public.client_scope(id);


--
-- Name: client_scope_role_mapping fk_cl_scope_rm_scope; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_scope_role_mapping
    ADD CONSTRAINT fk_cl_scope_rm_scope FOREIGN KEY (scope_id) REFERENCES public.client_scope(id);


--
-- Name: client_user_session_note fk_cl_usr_ses_note; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_user_session_note
    ADD CONSTRAINT fk_cl_usr_ses_note FOREIGN KEY (client_session) REFERENCES public.client_session(id);


--
-- Name: protocol_mapper fk_cli_scope_mapper; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.protocol_mapper
    ADD CONSTRAINT fk_cli_scope_mapper FOREIGN KEY (client_scope_id) REFERENCES public.client_scope(id);


--
-- Name: client_initial_access fk_client_init_acc_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.client_initial_access
    ADD CONSTRAINT fk_client_init_acc_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: component_config fk_component_config; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.component_config
    ADD CONSTRAINT fk_component_config FOREIGN KEY (component_id) REFERENCES public.component(id);


--
-- Name: component fk_component_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.component
    ADD CONSTRAINT fk_component_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: realm_default_groups fk_def_groups_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_default_groups
    ADD CONSTRAINT fk_def_groups_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: user_federation_mapper_config fk_fedmapper_cfg; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_federation_mapper_config
    ADD CONSTRAINT fk_fedmapper_cfg FOREIGN KEY (user_federation_mapper_id) REFERENCES public.user_federation_mapper(id);


--
-- Name: user_federation_mapper fk_fedmapperpm_fedprv; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_federation_mapper
    ADD CONSTRAINT fk_fedmapperpm_fedprv FOREIGN KEY (federation_provider_id) REFERENCES public.user_federation_provider(id);


--
-- Name: user_federation_mapper fk_fedmapperpm_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_federation_mapper
    ADD CONSTRAINT fk_fedmapperpm_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: associated_policy fk_frsr5s213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.associated_policy
    ADD CONSTRAINT fk_frsr5s213xcx4wnkog82ssrfy FOREIGN KEY (associated_policy_id) REFERENCES public.resource_server_policy(id);


--
-- Name: scope_policy fk_frsrasp13xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.scope_policy
    ADD CONSTRAINT fk_frsrasp13xcx4wnkog82ssrfy FOREIGN KEY (policy_id) REFERENCES public.resource_server_policy(id);


--
-- Name: resource_server_perm_ticket fk_frsrho213xcx4wnkog82sspmt; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT fk_frsrho213xcx4wnkog82sspmt FOREIGN KEY (resource_server_id) REFERENCES public.resource_server(id);


--
-- Name: resource_server_resource fk_frsrho213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_resource
    ADD CONSTRAINT fk_frsrho213xcx4wnkog82ssrfy FOREIGN KEY (resource_server_id) REFERENCES public.resource_server(id);


--
-- Name: resource_server_perm_ticket fk_frsrho213xcx4wnkog83sspmt; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT fk_frsrho213xcx4wnkog83sspmt FOREIGN KEY (resource_id) REFERENCES public.resource_server_resource(id);


--
-- Name: resource_server_perm_ticket fk_frsrho213xcx4wnkog84sspmt; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT fk_frsrho213xcx4wnkog84sspmt FOREIGN KEY (scope_id) REFERENCES public.resource_server_scope(id);


--
-- Name: associated_policy fk_frsrpas14xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.associated_policy
    ADD CONSTRAINT fk_frsrpas14xcx4wnkog82ssrfy FOREIGN KEY (policy_id) REFERENCES public.resource_server_policy(id);


--
-- Name: scope_policy fk_frsrpass3xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.scope_policy
    ADD CONSTRAINT fk_frsrpass3xcx4wnkog82ssrfy FOREIGN KEY (scope_id) REFERENCES public.resource_server_scope(id);


--
-- Name: resource_server_perm_ticket fk_frsrpo2128cx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_perm_ticket
    ADD CONSTRAINT fk_frsrpo2128cx4wnkog82ssrfy FOREIGN KEY (policy_id) REFERENCES public.resource_server_policy(id);


--
-- Name: resource_server_policy fk_frsrpo213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_policy
    ADD CONSTRAINT fk_frsrpo213xcx4wnkog82ssrfy FOREIGN KEY (resource_server_id) REFERENCES public.resource_server(id);


--
-- Name: resource_scope fk_frsrpos13xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_scope
    ADD CONSTRAINT fk_frsrpos13xcx4wnkog82ssrfy FOREIGN KEY (resource_id) REFERENCES public.resource_server_resource(id);


--
-- Name: resource_policy fk_frsrpos53xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_policy
    ADD CONSTRAINT fk_frsrpos53xcx4wnkog82ssrfy FOREIGN KEY (resource_id) REFERENCES public.resource_server_resource(id);


--
-- Name: resource_policy fk_frsrpp213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_policy
    ADD CONSTRAINT fk_frsrpp213xcx4wnkog82ssrfy FOREIGN KEY (policy_id) REFERENCES public.resource_server_policy(id);


--
-- Name: resource_scope fk_frsrps213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_scope
    ADD CONSTRAINT fk_frsrps213xcx4wnkog82ssrfy FOREIGN KEY (scope_id) REFERENCES public.resource_server_scope(id);


--
-- Name: resource_server_scope fk_frsrso213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_server_scope
    ADD CONSTRAINT fk_frsrso213xcx4wnkog82ssrfy FOREIGN KEY (resource_server_id) REFERENCES public.resource_server(id);


--
-- Name: composite_role fk_gr7thllb9lu8q4vqa4524jjy8; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.composite_role
    ADD CONSTRAINT fk_gr7thllb9lu8q4vqa4524jjy8 FOREIGN KEY (child_role) REFERENCES public.keycloak_role(id);


--
-- Name: user_consent_client_scope fk_grntcsnt_clsc_usc; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_consent_client_scope
    ADD CONSTRAINT fk_grntcsnt_clsc_usc FOREIGN KEY (user_consent_id) REFERENCES public.user_consent(id);


--
-- Name: user_consent fk_grntcsnt_user; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_consent
    ADD CONSTRAINT fk_grntcsnt_user FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- Name: group_attribute fk_group_attribute_group; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.group_attribute
    ADD CONSTRAINT fk_group_attribute_group FOREIGN KEY (group_id) REFERENCES public.keycloak_group(id);


--
-- Name: group_role_mapping fk_group_role_group; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.group_role_mapping
    ADD CONSTRAINT fk_group_role_group FOREIGN KEY (group_id) REFERENCES public.keycloak_group(id);


--
-- Name: realm_enabled_event_types fk_h846o4h0w8epx5nwedrf5y69j; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_enabled_event_types
    ADD CONSTRAINT fk_h846o4h0w8epx5nwedrf5y69j FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: realm_events_listeners fk_h846o4h0w8epx5nxev9f5y69j; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_events_listeners
    ADD CONSTRAINT fk_h846o4h0w8epx5nxev9f5y69j FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: identity_provider_mapper fk_idpm_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.identity_provider_mapper
    ADD CONSTRAINT fk_idpm_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: idp_mapper_config fk_idpmconfig; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.idp_mapper_config
    ADD CONSTRAINT fk_idpmconfig FOREIGN KEY (idp_mapper_id) REFERENCES public.identity_provider_mapper(id);


--
-- Name: web_origins fk_lojpho213xcx4wnkog82ssrfy; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.web_origins
    ADD CONSTRAINT fk_lojpho213xcx4wnkog82ssrfy FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- Name: scope_mapping fk_ouse064plmlr732lxjcn1q5f1; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.scope_mapping
    ADD CONSTRAINT fk_ouse064plmlr732lxjcn1q5f1 FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- Name: protocol_mapper fk_pcm_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.protocol_mapper
    ADD CONSTRAINT fk_pcm_realm FOREIGN KEY (client_id) REFERENCES public.client(id);


--
-- Name: credential fk_pfyr0glasqyl0dei3kl69r6v0; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.credential
    ADD CONSTRAINT fk_pfyr0glasqyl0dei3kl69r6v0 FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- Name: protocol_mapper_config fk_pmconfig; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.protocol_mapper_config
    ADD CONSTRAINT fk_pmconfig FOREIGN KEY (protocol_mapper_id) REFERENCES public.protocol_mapper(id);


--
-- Name: default_client_scope fk_r_def_cli_scope_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.default_client_scope
    ADD CONSTRAINT fk_r_def_cli_scope_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: required_action_provider fk_req_act_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.required_action_provider
    ADD CONSTRAINT fk_req_act_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: resource_uris fk_resource_server_uris; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.resource_uris
    ADD CONSTRAINT fk_resource_server_uris FOREIGN KEY (resource_id) REFERENCES public.resource_server_resource(id);


--
-- Name: role_attribute fk_role_attribute_id; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.role_attribute
    ADD CONSTRAINT fk_role_attribute_id FOREIGN KEY (role_id) REFERENCES public.keycloak_role(id);


--
-- Name: realm_supported_locales fk_supported_locales_realm; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.realm_supported_locales
    ADD CONSTRAINT fk_supported_locales_realm FOREIGN KEY (realm_id) REFERENCES public.realm(id);


--
-- Name: user_federation_config fk_t13hpu1j94r2ebpekr39x5eu5; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_federation_config
    ADD CONSTRAINT fk_t13hpu1j94r2ebpekr39x5eu5 FOREIGN KEY (user_federation_provider_id) REFERENCES public.user_federation_provider(id);


--
-- Name: user_group_membership fk_user_group_user; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.user_group_membership
    ADD CONSTRAINT fk_user_group_user FOREIGN KEY (user_id) REFERENCES public.user_entity(id);


--
-- Name: policy_config fkdc34197cf864c4e43; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.policy_config
    ADD CONSTRAINT fkdc34197cf864c4e43 FOREIGN KEY (policy_id) REFERENCES public.resource_server_policy(id);


--
-- Name: identity_provider_config fkdc4897cf864c4e43; Type: FK CONSTRAINT; Schema: public; Owner: keycloak
--

ALTER TABLE ONLY public.identity_provider_config
    ADD CONSTRAINT fkdc4897cf864c4e43 FOREIGN KEY (identity_provider_id) REFERENCES public.identity_provider(internal_id);


--
-- PostgreSQL database dump complete
--

\unrestrict YQtOAGJfyUp3ReEqvXOf2hjP5qaIVfNxmkPMXSf4oGyIFKmg5xpfqqKSS3vjEm7

