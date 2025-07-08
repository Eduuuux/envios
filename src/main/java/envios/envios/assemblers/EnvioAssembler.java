package envios.envios.assemblers;


import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import org.springframework.stereotype.Component;

import envios.envios.controller.EnvioController;
import envios.envios.model.Envio;

@Component
public class EnvioAssembler implements RepresentationModelAssembler<Envio, EntityModel<Envio>> {


@Override
    public EntityModel<Envio> toModel(Envio envio) {
        return EntityModel.of(envio,
            linkTo(methodOn(EnvioController.class).getAll()).withRel("envios"),
            linkTo(methodOn(EnvioController.class).createEnvio(null)).withRel("create"),
            linkTo(methodOn(EnvioController.class).updateEnvioPorNumero(envio.getNumeroEnvio(), null)).withRel("updateByNumero"),
            linkTo(methodOn(EnvioController.class).deleteEnvio(envio.getIdEnvio())).withRel("delete"),
            linkTo(methodOn(EnvioController.class).getByNumeroEnvio(envio.getNumeroEnvio())).withRel("findByNumero")
        );
    }
}

    