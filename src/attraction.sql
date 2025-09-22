-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Hôte : 127.0.0.1:3306
-- Généré le : dim. 27 avr. 2025 à 14:19
-- Version du serveur : 5.7.40
-- Version de PHP : 8.0.26

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de données : `attraction`
--

-- --------------------------------------------------------

--
-- Structure de la table `attraction`
--

DROP TABLE IF EXISTS `attraction`;
CREATE TABLE IF NOT EXISTS `attraction` (
  `id_attraction` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(100) NOT NULL,
  `type` varchar(50) NOT NULL,
  `description` text,
  `capacite` int(11) NOT NULL,
  `duree` time NOT NULL,
  `prix` int(10) NOT NULL,
  `statut` enum('active','inactive') DEFAULT 'active',
  PRIMARY KEY (`id_attraction`)
) ENGINE=MyISAM AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `attraction`
--

INSERT INTO `attraction` (`id_attraction`, `nom`, `type`, `description`, `capacite`, `duree`, `prix`, `statut`) VALUES
(1, 'Att1', 'Parcours', '', 22, '00:00:22', 7, 'active'),
(2, 'Att2', 'Manège', '', 50, '00:00:10', 5, 'active'),
(3, 'Att3', 'Parcours', '', 10, '00:00:15', 4, 'active'),
(4, 'Att4', 'Spectacle', '', 30, '00:00:20', 6, 'active'),
(5, 'Att5', 'Autre', NULL, 20, '00:00:10', 8, 'active');

-- --------------------------------------------------------

--
-- Structure de la table `billet`
--

DROP TABLE IF EXISTS `billet`;
CREATE TABLE IF NOT EXISTS `billet` (
  `id_billet` int(11) NOT NULL AUTO_INCREMENT,
  `id_reservation` int(11) DEFAULT NULL,
  `statut` enum('valide','utilise','expire') DEFAULT 'valide',
  PRIMARY KEY (`id_billet`),
  KEY `id_reservation` (`id_reservation`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `historique`
--

DROP TABLE IF EXISTS `historique`;
CREATE TABLE IF NOT EXISTS `historique` (
  `id_historique` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) DEFAULT NULL,
  `id_reservation` int(11) DEFAULT NULL,
  `date_visite` date DEFAULT NULL,
  PRIMARY KEY (`id_historique`),
  KEY `id_user` (`id_user`),
  KEY `id_reservation` (`id_reservation`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `paiement`
--

DROP TABLE IF EXISTS `paiement`;
CREATE TABLE IF NOT EXISTS `paiement` (
  `id_paiement` int(11) NOT NULL AUTO_INCREMENT,
  `id_reservation` int(11) DEFAULT NULL,
  `montant` decimal(10,2) DEFAULT NULL,
  `statut` enum('en_attente','effectue','refuse') DEFAULT 'en_attente',
  `date_paiement` date DEFAULT NULL,
  PRIMARY KEY (`id_paiement`),
  KEY `id_reservation` (`id_reservation`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `reduction`
--

DROP TABLE IF EXISTS `reduction`;
CREATE TABLE IF NOT EXISTS `reduction` (
  `id_reduction` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) DEFAULT NULL,
  `pourcentage` decimal(5,2) DEFAULT NULL,
  `raison` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id_reduction`),
  KEY `id_user` (`id_user`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- Structure de la table `reservation`
--

DROP TABLE IF EXISTS `reservation`;
CREATE TABLE IF NOT EXISTS `reservation` (
  `id_reservation` int(11) NOT NULL AUTO_INCREMENT,
  `id_user` int(11) DEFAULT NULL,
  `id_attraction` int(11) DEFAULT NULL,
  `date_reservation` date DEFAULT NULL,
  `statut` enum('en_attente','confirmee','annulee') DEFAULT 'en_attente',
  `invite_nom` varchar(20) DEFAULT NULL,
  `invite_email` varchar(20) DEFAULT NULL,
  PRIMARY KEY (`id_reservation`),
  KEY `id_user` (`id_user`),
  KEY `id_attraction` (`id_attraction`)
) ENGINE=MyISAM AUTO_INCREMENT=26 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `reservation`
--

INSERT INTO `reservation` (`id_reservation`, `id_user`, `id_attraction`, `date_reservation`, `statut`, `invite_nom`, `invite_email`) VALUES
(1, 7, 1, '2025-04-29', 'confirmee', '', ''),
(2, 8, 2, '2025-04-30', 'annulee', '', ''),
(3, 9, 4, '2025-05-27', 'annulee', '', ''),
(4, 7, 2, '2025-04-26', 'en_attente', '', ''),
(5, 8, 2, '2025-04-29', 'en_attente', '', ''),
(6, 9, 1, '2025-05-09', 'en_attente', '', ''),
(7, 7, 2, '2025-06-18', 'en_attente', '', ''),
(8, 1, 2, '2025-04-27', 'en_attente', '', ''),
(9, 1, 1, '2025-04-26', 'en_attente', '', ''),
(10, NULL, 2, '2025-04-24', 'en_attente', 'test invite nom', 'test invité email'),
(11, NULL, 1, '2025-04-24', 'en_attente', 'invite nom', 'invite mail'),
(12, 7, 3, '2025-05-09', 'annulee', NULL, NULL),
(13, 8, 3, '2025-06-08', 'annulee', NULL, NULL),
(14, 9, 1, '2025-05-08', 'confirmee', NULL, NULL),
(15, NULL, 1, '2025-04-30', 'en_attente', 'samedi', 'samedi@mail.fr'),
(16, 7, 5, '2025-05-10', 'annulee', NULL, NULL),
(17, 9, 5, '2025-05-11', 'confirmee', NULL, NULL),
(18, NULL, 4, '2025-05-09', 'en_attente', 'test nom', 'test email'),
(19, NULL, 4, '2025-05-04', 'en_attente', 'test encore', 'test encore'),
(20, NULL, 1, '2025-07-08', 'confirmee', 'moi', 'moi'),
(21, 8, 2, '2025-05-29', 'confirmee', NULL, NULL),
(22, NULL, 1, '2025-06-09', 'confirmee', 'moi', 'moi'),
(23, NULL, 3, '2025-05-03', 'confirmee', 'moi', 'moi'),
(24, NULL, 3, '2025-07-01', 'confirmee', 'test encore', 'test encore'),
(25, 9, 4, '2025-05-10', 'confirmee', NULL, NULL);

-- --------------------------------------------------------

--
-- Structure de la table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id_user` int(11) NOT NULL AUTO_INCREMENT,
  `nom` varchar(50) NOT NULL,
  `prenom` varchar(50) NOT NULL,
  `date_naissance` date DEFAULT NULL,
  `email` varchar(100) NOT NULL,
  `tel` varchar(20) NOT NULL,
  `mdp` varchar(255) NOT NULL,
  `type_client` enum('ENFANT','ADULTE','SENIOR','INVITE') DEFAULT NULL,
  `role` enum('client','admin') NOT NULL,
  `reduction` float DEFAULT NULL,
  PRIMARY KEY (`id_user`),
  UNIQUE KEY `email` (`email`)
) ENGINE=MyISAM AUTO_INCREMENT=12 DEFAULT CHARSET=utf8;

--
-- Déchargement des données de la table `user`
--

INSERT INTO `user` (`id_user`, `nom`, `prenom`, `date_naissance`, `email`, `tel`, `mdp`, `type_client`, `role`, `reduction`) VALUES
(1, 'ZHU', 'Cécilia', '2004-07-04', 'zhuce@edu.ece.fr', '123456789', '123', 'ADULTE', 'client', 0.15),
(6, 'testadmin', 'testadmin', '2004-07-04', 'test admin', 'zgge', '123', 'ADULTE', 'admin', 0.15),
(7, 'zhu', 'test', '2014-07-04', 'test enfant', 'test', '123', 'ENFANT', 'client', 0.5),
(8, 'test test', 'test test', '2014-07-12', 'test client test', '1234567890', '456', 'ENFANT', 'client', 0.5),
(9, 'test client2', 'test client 2', '1940-04-17', 'test client2', '123456789', '123', 'SENIOR', 'client', 0.3),
(10, 'Dubois', 'Pierre', '2000-04-23', 'dubois.pierre@mail.fr', '1234567890', '123', 'INVITE', 'admin', 0),
(11, 'yryr', 'ruru', '2004-04-17', 'fuu', '123456789', '123', 'INVITE', 'admin', 0);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
